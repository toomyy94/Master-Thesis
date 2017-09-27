/*
 * ttaenc.c
 *
 * Description: TTA lossless audio encoder/decoder.
 * Developed by: Aleksander Djuric <ald@iszf.irk.ru>
 *               Pavel Zhilin <pzh@iszf.irk.ru>
 *
 * Copyright (c) 1999-2003 Aleksander Djuric. All rights reserved.
 * Distributed under the GNU Lesser General Public License (LGPL).
 * The complete text of the license can be found in the COPYING
 * file included in the distribution.
 *
 */

#ifdef _WIN32
	#pragma  pack(1)
	#include <windows.h>
#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "ttaenc.h"
#include "entropy.h"
#include "filters.h"
#include <jni.h>
#include <android/log.h>
#define APPNAME "ArQoS.ttaEnc"

JNIEXPORT jint JNICALL
Java_pt_ptinovacao_arqospocket_core_voicecall_NativeEncoder_encodeWav(
		JNIEnv *env,        /* interface pointer */
		jobject obj,        /* "this" pointer */
		jstring path, jint decOrEnc)       /* path to wav file to encode */
{

	/* Obtain a C-copy of the Java string */

	const char *str = (*env)->GetStringUTFChars(env, path, 0);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Lets encode %s", str);
	if (encodeFiles(str, decOrEnc) == 0)
    {
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Encode Done");
        return 0;
    }

    __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Encode Error");
    return -1;
}


#ifdef _WIN32
	#include <direct.h>
	#include <io.h>
#else

#include <unistd.h>
#endif

struct {
    uint8_t	TTAid[3];
    uint8_t	TTAFormat;
    uint16_t	CompressLevel;
    uint16_t	AudioFormat;
    uint16_t	NumChannels;
    uint16_t	BitsPerSample;
    uint32_t	SampleRate;
    uint32_t	DataLength;
} __attribute__((packed)) tta_hdr;

struct {
    uint32_t	CRC32;
    uint32_t	DataLength;
} __attribute__((packed)) blk_hdr;

struct {
	uint8_t	id[3];
    uint16_t version;
    uint8_t	flags;
    uint8_t	size[4];
}  __attribute__((packed)) id3v2;

struct {
    uint32_t	ChunkID;
    uint32_t	ChunkSize;
    uint32_t	Format;
    uint32_t	Subchunk1ID;
    uint32_t	Subchunk1Size;
    uint16_t	AudioFormat;
    uint16_t	NumChannels;
    uint32_t	SampleRate;
    uint32_t	ByteRate;
    uint16_t	BlockAlign;
	uint16_t    BitsPerSample;
} __attribute__((packed)) wave_hdr;

struct {
    uint32_t	SubchunkID;
    uint32_t	SubchunkSize;
} __attribute__((packed)) subchunk_hdr;

struct flist;
struct flist {
	int8_t fname[_MAX_FNAME];
	struct flist *next;
};

static struct flist *files_list = NULL;
static struct flist *files_list_tail = NULL;

static FILE	*fdin, *fdout;
static int8_t	file_in[_MAX_FNAME];
static int8_t	file_out[_MAX_FNAME];
static int8_t	out_path[_MAX_FNAME];

static uint32_t	crc_table[256];
static uint32_t	input_byte_count;
static uint32_t	output_byte_count;
static uint32_t	tta_level = DEF_LEVEL;
static uint32_t	show_stat = 1;
static uint32_t	fixed_out = 0;

static uint64	total_input_bytes;
static uint64	total_output_bytes;

int
tta_error (int32_t error, const char* name) {
	switch (error) {
	case COMMAND_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Error:   unknown command '%s'\n%s\n", name, LINE); return 0;
	case FORMAT_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Error:   not compatible file format\n%s\n", LINE); return 0;
	case FIND_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Error:   file(s) not found '%s'\n%s\n\n", name, LINE); return -1;
	case CREATE_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Error:   problem creating directory '%s'\n%s\n\n", name, LINE); return -1;
	case OPEN_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Error:   can't open file '%s'\n%s\n\n", name, LINE); return -1;
	case FILE_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "\nError:   file is corrupted\n%s\n", LINE); return 0;
	case WRITE_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "\nError:   can't write to output file\n%s\n\n", LINE); return -1;
	case READ_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "\nError:   can't read from input file\n%s\n\n", LINE); return -1;
	case MEMORY_ERROR:
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "\nError:   insufficient memory available\n%s\n\n", LINE); return -1;
	}
}

void *
malloc1d (size_t num, size_t size) {
	void	*array;

	if ((array = calloc (num, size)) == NULL)
    {
        tta_error (MEMORY_ERROR, NULL);
        return NULL;
    }

	return (array);
}

int32_t **
malloc2d (int32_t num, uint32_t len) {
	int32_t	i, **array;

    array = (int32_t **) malloc(sizeof(int32_t *) * num);
    if (array == NULL)
    {
        tta_error (MEMORY_ERROR, NULL);
        return NULL;
    }
    for (i= 0; i < num; i++)
    {
        array[i] = (int32_t *) malloc(sizeof(int32_t) * len);
        if (array[i] == NULL)
        {
            tta_error (MEMORY_ERROR, NULL);
            return NULL;
        }
    }

	/*array = (int32_t **) calloc (num, sizeof(int32_t *) + len * sizeof(int32_t));
	if (array == NULL)
    {
        tta_error (MEMORY_ERROR, NULL);
        return NULL;
    }

	for(i = 0, tmp = (int32_t *) array + num; i < num; i++)
		array[i] = tmp + i * len;*/
	
	return (array);
}

static char *
getname (char *filename) {
	static int8_t showname[_MAX_FNAME];
	char *p;

	if ((p = strrchr (filename, _SEP))) p++;
	else p = filename;

	strncpy (showname, p, _MAX_FNAME-1);

	return showname;
}

static void
crc32_table_init (void) {
	uint32_t	i, j, crc;
	uint32_t	poly = 0xEDB88320L;

	for (i = 0; i < 256; i++) {
		for (crc = i, j = 8; j; j--)
			if (crc & 1) crc = (crc >> 1) ^ poly;
				else crc >>= 1;
		crc_table[i] = crc;
	}
}

static uint32_t 
crc32 (uint8_t *buffer, uint32_t len) {
	uint32_t	i;
	uint32_t	crc = 0xFFFFFFFF;

	for (i = 0; i < len; i++)
		crc = ((crc>>8) & 0x00FFFFFF) ^ crc_table[(crc^buffer[i]) & 0xFF];

	return (crc^0xFFFFFFFF);
}

static size_t
read_wave (int32_t *data, uint32_t byte_size, uint32_t len, FILE *fdin) {
	size_t i, bytes_read = 0;
    void	*buffer;

	buffer = malloc1d (len + 2, byte_size);

	switch (byte_size) {
	case 1: {
				uint8_t *sbuffer = buffer;
				if ((bytes_read = fread (sbuffer, byte_size, len, fdin)) == 0) 
                {
                    tta_error (READ_ERROR, NULL);
                    return 0;
                }
				for (i = 0; i < bytes_read; i++) data[i] = (int32_t) sbuffer[i] - 0x80;
				break;
			}
	case 2:	{
				int16_t *sbuffer = buffer;
				if ((bytes_read = fread (sbuffer, byte_size, len, fdin)) == 0)
                {
                    tta_error (READ_ERROR, NULL);
                    return 0;
                }
				for (i = 0; i < bytes_read; i++) data[i] = (int32_t) sbuffer[i];
				break;	
			}
	case 3:	{
				uint8_t *sbuffer = buffer;
				if ((bytes_read = fread (sbuffer, byte_size, len, fdin)) == 0)
                {
                    tta_error (READ_ERROR, NULL);
                    return 0;
                }
				for (i = 0; i < bytes_read; i++) {
					uint32_t t = *((int32_t *)(sbuffer + i * byte_size));
					data[i] = (int32_t) (t << 8) >> 8;
				}
				break;
			}
	case 4:	{ 
				if ((bytes_read = fread (data, byte_size, len, fdin)) == 0)
                {
                    tta_error (READ_ERROR, NULL);
                    return 0;
                }
				break;
			}
        default:
            //oh shit
            bytes_read = 0;
	}

	free (buffer);
	return (bytes_read);
}

static int32_t
write_wave (int32_t **data, int32_t byte_size, int32_t num_chan, uint32_t len, FILE *fdout) {
	int32_t	n;
	uint32_t	i, bytes_wrote = 0;
    void	*buffer;

	buffer = malloc1d (len * num_chan + 2, byte_size);

	switch (byte_size) {
	case 1: {
				uint8_t  *sbuffer = buffer;
				for (i = 0; i < (len * num_chan); i+= num_chan)
				for (n = 0; n < num_chan; n++) sbuffer[i+n] = (uint8_t) (data[n][i/num_chan] + 0x80);
				bytes_wrote = fwrite (sbuffer, byte_size, len * num_chan, fdout);
				if (bytes_wrote == 0)
                {
                    tta_error (WRITE_ERROR, NULL);
                    return 0;
                }
				break;
			}
	case 2: {
				int16_t *sbuffer = buffer;
				for (i = 0; i < (len * num_chan); i+= num_chan)
				for (n = 0; n < num_chan; n++) sbuffer[i+n] = (int16_t) data[n][i/num_chan];
				bytes_wrote = fwrite (sbuffer, byte_size, len * num_chan, fdout);
				if (bytes_wrote == 0)
                {
                    tta_error (WRITE_ERROR, NULL);
                    return 0;
                }
				break;
			}
	case 3: {
				uint8_t *sbuffer = buffer;
				for (i = 0; i < (len * num_chan); i+= num_chan)
				for (n = 0; n < num_chan; n++) 
					*((int32_t *)(sbuffer + (i+n) * byte_size)) = data[n][i/num_chan];
				bytes_wrote = fwrite (sbuffer, byte_size, len * num_chan, fdout);
				if (bytes_wrote == 0)
                {
                    tta_error (WRITE_ERROR, NULL);
                    return 0;
                }
				break;
			}
	case 4: {
				int32_t *sbuffer = buffer;
				for (i = 0; i < (len * num_chan); i+= num_chan)
				for (n = 0; n < num_chan; n++) sbuffer[i+n] = data[n][i/num_chan];
				bytes_wrote = fwrite (sbuffer, byte_size, len * num_chan, fdout);
				if (bytes_wrote == 0)
                {
                    tta_error (WRITE_ERROR, NULL);
                    return 0;
                }
				break;
			}
	}

	free (buffer);
	return (bytes_wrote);
}

void
split_int (int32_t *data, int32_t frame_len, int32_t num_chan, int32_t **buffer) {
	int32_t	i, j, n;

	for (i = 0; i < frame_len; i++)
	for (j = 0; j < num_chan; j++) {
		buffer[j][i] = data[i * num_chan + j];
	}

	if (num_chan > 1)
	for (i = 0, n = (num_chan - 1); i < frame_len; i++) {
		for (j = 0; j < n; j++)
			buffer[j][i] = buffer[j+1][i] - buffer[j][i];
		buffer[n][i] = buffer[n][i] - (buffer[n-1][i] / 2);
	}
}

void
split_float (int32_t *data, int32_t frame_len, int32_t num_chan, int32_t **buffer) {
	int32_t	i, j;

	for (i = 0; i < frame_len; i++) 
	for (j = 0; j < num_chan; j++) {
		uint32_t t = data[i * num_chan + j];
		uint32_t negative = (t & 0x80000000)? -1:1;
		uint32_t data_hi = (t & 0x7FFF0000) >> 16;
		uint32_t data_lo = (t & 0x0000FFFF);

		buffer[j][i] = (data_hi || data_lo)? (data_hi - 0x3F80):0;
		buffer[j+num_chan][i] = (SWAP16(data_lo) + 1) * negative;
	}
}

void _debug_format_field(const char* name, int32_t expected, int32_t got)
{
    __android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s: expected %.4s but got %.4s\n", name, (char*)&expected, (char*)&got);
}

int
compress (FILE *fdin, FILE *fdout) {
	int32_t	*data, **buffer;
	uint32_t i, num_chan, data_size, byte_size;
    uint32_t	len, frame_size, frame_len, bit_array_size;
    uint32_t	is_float, file_size, read_bytes;
	time_t	stime = time(NULL);

	// clear statistics
	input_byte_count = output_byte_count = 0;

	// print process banner
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Encode:  processing ..\r");

	// get file size
	fseek (fdin, 0, SEEK_END);
	file_size = ftell (fdin);
	fseek (fdin, 0, SEEK_SET);

    __android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "compress: fdin has %lu bytes\n", file_size);

	// read WAVE header
    read_bytes = fread (&wave_hdr, sizeof (wave_hdr), 1, fdin);
	if (ferror(fdin))
    {
        tta_error (READ_ERROR, NULL);
        return 1;
    }

    __android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "compress: read %lu bytes for header (expected %d)\n", read_bytes, ((int)sizeof (wave_hdr)));

	// check for supported formats
	if ((wave_hdr.ChunkID		!= RIFF_SIGN) ||
		(wave_hdr.Format		!= WAVE_SIGN) ||
		(wave_hdr.Subchunk1ID	!= fmt_SIGN) ||
		(wave_hdr.Subchunk1Size	> wave_hdr.ChunkSize) ||
		(wave_hdr.NumChannels	== 0) ||
		(wave_hdr.BitsPerSample	> MAX_BPS)) {
		tta_error (FORMAT_ERROR, NULL);
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "format1");
        uint32_t tmp_l;
        _debug_format_field("wave_hdr.ChunkID", RIFF_SIGN, wave_hdr.ChunkID);
        _debug_format_field("wave_hdr.Format", WAVE_SIGN, wave_hdr.Format);
        _debug_format_field("wave_hdr.Subchunk1ID", fmt_SIGN, wave_hdr.Subchunk1ID);
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "wave_hdr.Subchunk1Size: %u wave_hdr.ChunkSize: %u", wave_hdr.Subchunk1Size, wave_hdr.ChunkSize);
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "wave_hdr.NumChannels: %hu", wave_hdr.NumChannels);
        __android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "wave_hdr.BitsPerSample: %hu (expected %d)", wave_hdr.BitsPerSample, MAX_BPS);
		return 1;
	}

	switch (wave_hdr.AudioFormat) {
	case WAVE_FORMAT_IEEE_FLOAT:
		is_float = 1; break;
	case WAVE_FORMAT_PCM:
		is_float = 0; break;
	default:
		tta_error (FORMAT_ERROR, NULL);
		return 1;
	}

	if ((is_float && wave_hdr.BitsPerSample != MAX_BPS) ||
		(!is_float && wave_hdr.BitsPerSample == MAX_BPS)) {
		tta_error (FORMAT_ERROR, NULL);
		return 1;
	}

	// skip extra format bytes
	if (wave_hdr.Subchunk1Size > 16) {
		fseek (fdin, wave_hdr.Subchunk1Size - 16, SEEK_CUR);
		__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Encode:  skiped %d extra format bytes\r\n",
			(int) wave_hdr.Subchunk1Size - 16);
	}


	// skip unsupported chunks
	while (fread (&subchunk_hdr, sizeof (subchunk_hdr), 1, fdin) &&
			subchunk_hdr.SubchunkID != data_SIGN) {
		int8_t chunk_id[5];
		if (ferror(fdin))
        {
            tta_error (READ_ERROR, NULL);
            return 1;
        }
		if (subchunk_hdr.SubchunkSize > (file_size - ftell (fdin))) {
			tta_error (FILE_ERROR, NULL);
            return 1;
		}
		memcpy(chunk_id, &subchunk_hdr.SubchunkID, 4);
		chunk_id[4] = 0;
		fseek (fdin, subchunk_hdr.SubchunkSize, SEEK_CUR);
		__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Encode:  skiped unsupported '%s' chunk\r\n",
			chunk_id);
	}


	frame_size	= (int32_t) (FRAME_TIME * wave_hdr.SampleRate);
	num_chan	= wave_hdr.NumChannels;
	data_size	= subchunk_hdr.SubchunkSize;
	byte_size	= (wave_hdr.BitsPerSample + 7) / 8;
	len			= data_size/byte_size;

	input_byte_count = ftell (fdin);
	memcpy(tta_hdr.TTAid, TTA_SIGN, 3);

	tta_hdr.TTAFormat		= TTA_FORMAT;
	tta_hdr.CompressLevel	= (int16_t) tta_level;
	tta_hdr.AudioFormat		= wave_hdr.AudioFormat;
	tta_hdr.NumChannels		= wave_hdr.NumChannels;
	tta_hdr.BitsPerSample	= wave_hdr.BitsPerSample;
	tta_hdr.SampleRate		= wave_hdr.SampleRate;
	tta_hdr.DataLength		= len / num_chan;

	// grab some space for a buffers
	data = (int32_t *) malloc1d (num_chan * frame_size, sizeof (int32_t));
	buffer = malloc2d (num_chan << is_float, frame_size);


	// write TTA header
	if (fwrite (&tta_hdr, sizeof(tta_hdr), 1, fdout) == 0)
    {
        tta_error (WRITE_ERROR, NULL);
        return 1;
    }
	else output_byte_count += sizeof(tta_hdr);



	while (len > 0) {
        frame_len = num_chan * frame_size;
        frame_len = (frame_len <= len) ? frame_len : len;
        frame_len = (uint32_t)read_wave(data, byte_size, frame_len, fdin);

        len -= frame_len;
        frame_len /= num_chan;

        if (is_float)
        {
            split_float(data, frame_len, num_chan, buffer);
        }
		else{
            split_int (data, frame_len, num_chan, buffer);
        }

		if (init_bit_array_write () != 0){
            return 1;
        }


		// compress block
		for (i = 0; i < (num_chan << is_float); i++) {
			filters_compress (buffer[i], frame_len, tta_level, byte_size);
			encode_frame (buffer[i], frame_len);			
		}
		
		// write block header
		blk_hdr.DataLength	= bit_array_size = get_len ();
		blk_hdr.CRC32 		= crc32 (bit_array, bit_array_size);

		if (fwrite (&blk_hdr, sizeof(blk_hdr), 1, fdout) == 0)
        {
            tta_error (WRITE_ERROR, NULL);
            return 1;
        }
		else output_byte_count += sizeof(blk_hdr);

		// write compressed data
		if (fwrite (bit_array, bit_array_size, 1, fdout) == 0)
        {
            tta_error (WRITE_ERROR, NULL);
            return 1;
        }
		else output_byte_count += bit_array_size;

		free (bit_array);

		input_byte_count += (num_chan * byte_size * frame_len);

		if (show_stat) {

		ERASE_STDERR;
		__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Encode:  wrote %d bytes, %d%% complete, ratio: %.2f, time: %d\r",
			(int) output_byte_count,
			(int) ((float) input_byte_count/ (data_size + 1) * 100),
			(float) output_byte_count/(input_byte_count + 1),
			(int) (time (NULL) - stime));
		}
	}

    for (i = 0; i < (num_chan << is_float); i++)
        free(buffer[i]);
	free (buffer);
	free (data);

	ERASE_STDERR;
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Encode:  wrote %d bytes, done, ratio: %.2f, time: %d\n",
		(int) output_byte_count,
		(float) output_byte_count/(input_byte_count + 1),
		(int) (time (NULL) - stime));

	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s\n", LINE);

	return 0;
}

void
combine_int (int32_t frame_len, int32_t num_chan, int32_t **buffer) {
	int32_t	i, j, n;

	if (num_chan > 1)
	for (i = 0, n = (num_chan - 1); i < frame_len; i++) {
		buffer[n][i] = buffer[n][i] + (buffer[n-1][i] / 2);
		for (j = n; j > 0; j--)
			buffer[j-1][i] = buffer[j][i] - buffer[j-1][i];
	}
}

void
combine_float (int32_t frame_len, int32_t num_chan, int32_t **buffer) {
	int32_t	i, j;

	for (i = 0; i < frame_len; i++) 
	for (j = 0; j < num_chan; j++) {
		uint32_t negative = buffer[j+num_chan][i] & 0x80000000;
		uint32_t data_hi = buffer[j][i];
		uint32_t data_lo = abs(buffer[j+num_chan][i]) - 1;
		
		data_hi += (data_hi || data_lo)? 0x3F80:0;
		buffer[j][i] = (data_hi << 16) | SWAP16(data_lo) | negative;
	}
}

int
decompress (FILE *fdin, FILE *fdout) {
    int32_t	**buffer;
    uint32_t	i, num_chan, byte_size, data_size;
    uint32_t	is_float, len, frame_size, frame_len, bit_array_size;
	time_t	stime = time(NULL);

	// clear statistics
	input_byte_count = output_byte_count = 0;

	// skip ID3V2 header
	if (fread (&id3v2, sizeof(id3v2), 1, fdin) == 0)
    {
        tta_error (READ_ERROR, NULL);
        return 1;
    }
	if (!memcmp (id3v2.id, "ID3", 3)) {
		if (id3v2.size[0] & 0x80) {
			tta_error (FILE_ERROR, NULL);
            return 1;
		}
		len = (id3v2.size[0] & 0x7f);
		len = (len << 7) | (id3v2.size[1] & 0x7f);
		len = (len << 7) | (id3v2.size[2] & 0x7f);
		len = (len << 7) | (id3v2.size[3] & 0x7f);
		len += 10;
		if (id3v2.flags & (1 << 4)) len += 10;
		fseek (fdin, len, SEEK_SET);
	} else fseek (fdin, 0, SEEK_SET);

	// print process banner
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Decode:  processing ..\r");

	// read TTA header
	if (fread (&tta_hdr, sizeof (tta_hdr), 1, fdin) == 0)
    {
        tta_error (READ_ERROR, NULL);
        return 1;
    }
	else input_byte_count += sizeof(tta_hdr);

	// check for supported formats
	if ((memcmp (tta_hdr.TTAid, TTA_SIGN, 3)) ||
		(tta_hdr.TTAFormat		!= TTA_FORMAT) ||
		(tta_hdr.CompressLevel	< MIN_LEVEL) ||
		(tta_hdr.CompressLevel	> MAX_LEVEL) ||
		(tta_hdr.BitsPerSample	> MAX_BPS)) {
		tta_error (FORMAT_ERROR, NULL);
		return 1;
	}

	// check data length
	if (tta_hdr.DataLength	> (1 << 31)) {
		tta_error (FILE_ERROR, NULL);
        return 1;
	}

	tta_level		= tta_hdr.CompressLevel;
	byte_size		= (tta_hdr.BitsPerSample + 7) / 8;
	frame_size		= (int32_t) (FRAME_TIME * tta_hdr.SampleRate);
	num_chan		= tta_hdr.NumChannels;
	len				= tta_hdr.DataLength;
	data_size		= len * byte_size * num_chan;
	is_float		= (tta_hdr.AudioFormat == WAVE_FORMAT_IEEE_FLOAT);

	memset (&wave_hdr, 0, sizeof (wave_hdr));
	wave_hdr.ChunkID		= RIFF_SIGN;
	wave_hdr.ChunkSize		= data_size + 36;
	wave_hdr.Format			= WAVE_SIGN;
	wave_hdr.Subchunk1ID	= fmt_SIGN;
	wave_hdr.Subchunk1Size	= 16;
	wave_hdr.AudioFormat	= tta_hdr.AudioFormat;
	wave_hdr.NumChannels	= (uint16_t) num_chan;
	wave_hdr.SampleRate		= tta_hdr.SampleRate;
	wave_hdr.BitsPerSample	= tta_hdr.BitsPerSample;
	wave_hdr.ByteRate		= tta_hdr.SampleRate * byte_size * num_chan;
	wave_hdr.BlockAlign		= (uint16_t)(num_chan * byte_size);
	subchunk_hdr.SubchunkID	= data_SIGN;
	subchunk_hdr.SubchunkSize = data_size;

	// write WAVE header
	if (fwrite (&wave_hdr, sizeof (wave_hdr), 1, fdout) == 0)
    {
        tta_error (WRITE_ERROR, NULL);
        return 1;
    }
	else output_byte_count += sizeof(wave_hdr);

	// write Subchunk header
	if (fwrite (&subchunk_hdr, sizeof (subchunk_hdr), 1, fdout) == 0)
    {
        tta_error (WRITE_ERROR, NULL);
        return 1;
    }
	else output_byte_count += sizeof (subchunk_hdr);

	// grab some space for a buffer
	buffer = malloc2d (num_chan << is_float, frame_size);

	while (len > 0) {
		frame_len = (frame_size <= len)? frame_size:len;

		// read block header
		if (fread (&blk_hdr, sizeof(blk_hdr), 1, fdin) == 0)
        {
            tta_error (READ_ERROR, NULL);
            return 1;
        }
		else input_byte_count += sizeof(blk_hdr);
		if (blk_hdr.DataLength > (1 << 31)) {
			tta_error (FILE_ERROR, NULL);
            for (i = 0; i < (num_chan << is_float); i++)
                free(buffer[i]);
			free (buffer);
			return 1;
		}

		bit_array_size = blk_hdr.DataLength;
		if (init_bit_array_read (bit_array_size) != 0)
            return 1;

		// read block data
		if (fread (bit_array, 1, bit_array_size, fdin) == 0)
        {
            tta_error (READ_ERROR, NULL);
            return 1;
        }
		else input_byte_count += bit_array_size;

		// check for errors
		if (blk_hdr.CRC32 != crc32 (bit_array, bit_array_size)) {
			tta_error (FILE_ERROR, NULL);
			free (bit_array);
            for (i = 0; i < (num_chan << is_float); i++)
                free(buffer[i]);
			free (buffer);
			return 1;
		}

		for (i = 0; i < (num_chan << is_float); i++) {
			decode_frame (buffer[i], frame_len);
			filters_decompress (buffer[i], frame_len, tta_level, byte_size);
		}

		if (is_float)
			 combine_float (frame_len, num_chan, buffer);
		else combine_int (frame_len, num_chan, buffer);

		free (bit_array);

		output_byte_count += 
		    write_wave (buffer, byte_size, num_chan, frame_len, fdout) * byte_size;

		if (show_stat) {
		ERASE_STDERR;
		__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Decode:  wrote %d bytes, %d%% complete, ratio: %.2f, time: %d\r",
			(int) (output_byte_count),
			(int) ((float) output_byte_count/(data_size + 1) * 100),
			(float) output_byte_count/(input_byte_count + 1),
			(int) (time (NULL) - stime));
		}

		len -= frame_len;
	}

    for (i = 0; i < (num_chan << is_float); i++)
        free(buffer[i]);
	free (buffer);

	ERASE_STDERR;
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Decode:  wrote %d bytes, done, ratio: %.2f, time: %d\n",
		(int) (output_byte_count),
		(float) output_byte_count/(input_byte_count + 1),
		(int) (time (NULL) - stime));
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s\n", LINE);
	
	return 0;
}

static void
fill_out_name_with_extention (char *ext) {
	int32_t len;
	char *p, *filename;

	len = strlen (file_out);
	if (len && (file_out[len] != _SEP)) file_out[len++] = _SEP;
	filename = (strrchr (file_in, _SEP) == 0)? file_in:(strrchr (file_in, _SEP) + 1);

	strcat (file_out, filename);
	p = (strrchr (file_out, '.') == 0)? file_out:(strrchr (file_out, '.') + 1);
	strcpy (p, ext);
}

static void
add_to_files_list (char *path, struct flist **head, struct flist **tail) {
	struct flist *new_item;

	new_item = (struct flist *) malloc1d (1, sizeof(struct flist));
	strcpy (new_item->fname, path);
	new_item->next = NULL;
	if (*head == NULL) *head = *tail = new_item;
	else *tail = (*tail)->next = new_item;
}

static void
clear_files_list (void) {
	struct flist *item;

	while (files_list != NULL) {
		item = files_list;
		files_list = files_list->next;
		free (item);
	}
}

static void
usage (void) {
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "usage:\t" MYNAME " [command] [options] file(s).. <output path\\>\n\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s\n", LINE);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "commands:\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s\n", LINE);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "\t-e[N]\tencode file(s) with compression level\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "\t\tN = {1..3}, default recommended level {2}\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "\t-d\tdecode file(s)\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "\t-f name\tspecify output file name\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "\t-v\tshow codec version\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "\t-h\tthis help\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s\n", LINE);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "options:\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s\n", LINE);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "\t-u\tdelete source file if successful\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "\t-s\tsilent mode\n");
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s\n", LINE);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "examples:\tttaenc -e *.wav; ttaenc -d *.tta \\audio\n\n");

	exit (0);
}

static int
process_files (int32_t act, int32_t files, int32_t cls) {
	struct flist *item;
	time_t ftime = time(NULL);
	int8_t status[256];
	int count = 0;
	int processed = 0;
	int ret = 0;
    //int8_t buf[32768];

	crc32_table_init ();

	for (item = files_list; item != NULL; item = item->next) {

#ifdef _WIN32
		sprintf(status, "TTA: %d/%d - %d file(s) processed",
			count+1, files, processed);
		SetConsoleTitle(status);
#endif
		memcpy (file_in, item->fname, _MAX_FNAME);
		memcpy (file_out, out_path, _MAX_FNAME);


//		if (!fixed_out) switch (act) {
//		case 1: fill_out_name_with_extention ("tta"); break;
//		case 2: fill_out_name_with_extention ("wav"); break;
//		}

		// print file banner

		fdin  = fopen (file_in, "rb");
		if (!fdin)
        {
            tta_error (OPEN_ERROR, file_in);
            return -1;
        }

		fdout = fopen (file_out, "wb");
		if (!fdout)
        {
            tta_error (OPEN_ERROR, file_out);
            __android_log_print(ANDROID_LOG_DEBUG, APPNAME, "Erro 5 ao abrir %s", file_out);
            return -1;
        }
		setvbuf(fdout, NULL, _IOFBF, 32768);
        //setvbuf(fdout, buf, _IOFBF, 32768);
		
		switch (act) {
		case 1:	ret = compress (fdin, fdout); break;
		case 2: ret = decompress (fdin, fdout); break;
		}

		fflush (fdout);
		fclose (fdin);
		fclose (fdout);

		if (!ret) {
			total_output_bytes += output_byte_count;
			if (cls) unlink(file_in);
			processed++;
		}

		count++;
	}

#ifdef _WIN32
		sprintf(status, "TTA: %d/%d - %d file(s) processed",
			count, files, processed);
		SetConsoleTitle(status);
#endif

	ftime = (int) (time (NULL) - ftime);
	sprintf (status, "%d'%d", ftime / 60, ftime % 60);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "Total:   [%d/%d, %.1f/%.1f Mb], ratio: %.3f, time: %s\n",
		processed, files,
		(float) total_output_bytes / 1048576,
		(float) total_input_bytes / 1048576,
		(float) total_output_bytes/(total_input_bytes + 1), status);
	__android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "%s\n\n", LINE);

    return 0;
}

void fill_out_name_with_extension(char *ext) {
	int len;
	char *p, *filename;

	len = strlen(file_out);
	if (len && (file_out[len] != _SEP))
		file_out[len++] = _SEP;

	filename = strrchr(file_in, _SEP);
	filename = (!filename) ? file_in : (filename + 1);

	strcat(file_out, filename);
	p = strrchr(file_out, '.');
	if (p) *p = '\0';

	strcat(file_out, ext);
}

char *remove_filename_from_path(char *path) {
    char *p, *dirname;
    dirname = path;
    p = strrchr(dirname, _SEP);
    if (p) *p = '\0';

    return dirname;
}

int encodeFiles(const char *path_in, jint value) {
	int act = value, farg = 1;
	total_input_bytes = total_output_bytes = 0;
	*out_path = *file_in = *file_out = '\0';
    int ret = 0;

	//strcpy(file_out, "-");
	fixed_out = 0;
	strncpy(file_in, path_in, _MAX_FNAME - 1);
	add_to_files_list(file_in, &files_list, &files_list_tail);
	if (!fixed_out) {
		switch (act) {
			case 1:
				fill_out_name_with_extension(".tta");
				break;
			case 2:
				fill_out_name_with_extension(".wav");
				break;
		}

	}

	strcpy(out_path, file_in);
	remove_filename_from_path(out_path);
    snprintf(out_path + strlen(out_path), _MAX_FNAME - strlen(out_path), "/%s", file_out);
	strcpy(file_out, out_path);
    __android_log_print(ANDROID_LOG_DEBUG, APPNAME,  "encodeFiles: file_out: %s\n\n", file_out);
	if (process_files(act, farg, 0) != 0)
        ret = -1;

	clear_files_list();

	return ret;
}
/* eof */


