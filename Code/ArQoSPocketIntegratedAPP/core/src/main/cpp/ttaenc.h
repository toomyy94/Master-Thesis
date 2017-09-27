/*
 * ttaenc.h
 *
 * Description: TTA main definitions and prototypes
 * Developed by: Aleksander Djuric <ald@iszf.irk.ru>
 *               Pavel Zhilin <pzh@iszf.irk.ru>
 *
 * Copyright (c) 1999-2003 Aleksander Djuric. All rights reserved.
 * Distributed under the GNU Lesser General Public License (LGPL).
 * The complete text of the license can be found in the COPYING
 * file included in the distribution.
 *
 */

#ifndef TTAENC_H
#define TTAENC_H

#define COPYRIGHT		"Copyright (c) 2003 Aleksander Djuric. All rights reserved."

#define wstrncpy wcsncpy

#define MYNAME			"ttaenc-dev"
#define VERSION			"2.0-2003"
#define BUILD			"20040108"
#define FRAME_TIME		1.04489795918367346939
#define TTA_SIGN		"TTA"
#define TTA_FORMAT		'2'

#define MAX_BPS			32
#define MIN_LEVEL		1
#define MAX_LEVEL		3
#define DEF_LEVEL		2

#define WAVE_FORMAT_PCM	1
#define WAVE_FORMAT_IEEE_FLOAT 3

#define RIFF_SIGN		0x46464952
#define WAVE_SIGN		0x45564157
#define fmt_SIGN		0x20746D66
#define data_SIGN		0x61746164

#define COMMAND_ERROR	0
#define FORMAT_ERROR	1
#define FILE_ERROR		2
#define FIND_ERROR		3
#define CREATE_ERROR	4
#define OPEN_ERROR		5
#define WRITE_ERROR		6
#define READ_ERROR		7
#define MEMORY_ERROR	8

#define SWAP16(x) (\
(((x)&(1<< 0))?(1<<15):0) | \
(((x)&(1<< 1))?(1<<14):0) | \
(((x)&(1<< 2))?(1<<13):0) | \
(((x)&(1<< 3))?(1<<12):0) | \
(((x)&(1<< 4))?(1<<11):0) | \
(((x)&(1<< 5))?(1<<10):0) | \
(((x)&(1<< 6))?(1<< 9):0) | \
(((x)&(1<< 7))?(1<< 8):0) | \
(((x)&(1<< 8))?(1<< 7):0) | \
(((x)&(1<< 9))?(1<< 6):0) | \
(((x)&(1<<10))?(1<< 5):0) | \
(((x)&(1<<11))?(1<< 4):0) | \
(((x)&(1<<12))?(1<< 3):0) | \
(((x)&(1<<13))?(1<< 2):0) | \
(((x)&(1<<14))?(1<< 1):0) | \
(((x)&(1<<15))?(1<< 0):0))

#ifdef _WIN32
    #define _SEP '\\'
    #define ERASE_STDERR fprintf (stderr, "%78c\r", 0x20)
#else
    #define _MAX_FNAME 1024
    #define _SEP '/'
    #define ERASE_STDERR fprintf (stderr, "\033[2K")
#endif
#define LINE "------------------------------------------------------------"

int tta_error (int32_t error, const char *name);
void *malloc1d (size_t num, size_t size);
int32_t **malloc2d (int32_t num, uint32_t len);

#endif	/* TTAENC_H */
