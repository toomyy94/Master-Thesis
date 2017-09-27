/*
 * entropy.c
 *
 * Description: TTA entropy coding functions.
 * Developed by: Aleksander Djuric <ald@iszf.irk.ru>
 *               Pavel Zhilin <pzh@iszf.irk.ru>
 *
 * Copyright (c) 1999-2003 Aleksander Djuric. All rights reserved.
 * Distributed under the GNU Lesser General Public License (LGPL).
 * The complete text of the license can be found in the COPYING
 * file included in the distribution.
 *
 */

#include <stdlib.h>
#include "entropy.h"
#include "ttaenc.h"

static uint32_t bit_mask32[]= {
	0x00000000, 0x00000001, 0x00000003, 0x00000007,
	0x0000000f, 0x0000001f, 0x0000003f, 0x0000007f,
	0x000000ff, 0x000001ff, 0x000003ff, 0x000007ff,
	0x00000fff, 0x00001fff, 0x00003fff, 0x00007fff,
	0x0000ffff, 0x0001ffff, 0x0003ffff, 0x0007ffff,
	0x000fffff, 0x001fffff, 0x003fffff, 0x007fffff,
	0x00ffffff, 0x01ffffff, 0x03ffffff, 0x07ffffff,
	0x0fffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
	0xffffffff
};

static int32_t shift16[]= {
	0x00000010, 0x00000020, 0x00000040, 0x00000080,
	0x00000100, 0x00000200, 0x00000400, 0x00000800,
	0x00001000, 0x00002000, 0x00004000, 0x00008000,
	0x00010000, 0x00020000, 0x00040000, 0x00080000,
	0x00100000, 0x00200000, 0x00400000, 0x00800000,
	0x01000000, 0x02000000, 0x04000000, 0x08000000,
	0x10000000, 0x20000000, 0x40000000, 0x80000000
};

uint8_t *bit_array;
uint32_t bit_array_size, bit_array_bits;

int
init_bit_array_write(void) {
	bit_array = (uint8_t *) malloc1d (BASE_SIZE, sizeof(int8_t));
    if (bit_array == NULL)
        return -1;
	bit_array_bits = 0;
	bit_array_size = BASE_SIZE;
    return 0;
}

int
init_bit_array_read(uint32_t size) {
	bit_array_size = size;
	bit_array = (uint8_t *) malloc1d (bit_array_size, sizeof(int8_t));
    if (bit_array == NULL)
        return -1;
	bit_array_bits = 0;
    return 0;
}

int32_t
get_len (void) {
	return (bit_array_bits >> 3) + ((bit_array_bits & 7UL)? 1:0);
}

 int
put_binary (uint32_t value, uint32_t bits) {
	uint32_t fbit = bit_array_bits & 0x1FUL;
	uint32_t rbit = 32 - fbit;
	uint32_t pos = bit_array_bits >> 5;
	uint32_t *s = ((uint32_t *)bit_array) + pos;

	if ((pos << 2) + 5 > bit_array_size) {
		bit_array = (uint8_t *) realloc (bit_array, bit_array_size += STEP_SIZE);
		if (!bit_array)
        {
            tta_error (MEMORY_ERROR, NULL);
            return -1;
        }
	}
		
	*s &= bit_mask32[fbit];
	*s |= (value & bit_mask32[bits]) << fbit;
	if (bits > rbit) *(++s) = value >> rbit;

	bit_array_bits += bits;

     return 0;
}

int
put_unary (uint32_t value) {
	uint32_t fbit = bit_array_bits & 0x1FUL;
	uint32_t rbit = 32 - fbit;
	uint32_t pos = bit_array_bits >> 5;
	uint32_t *s = ((uint32_t *)bit_array) + pos;

	if ((pos << 2) + value > bit_array_size) {
		bit_array = (uint8_t *) realloc (bit_array, bit_array_size += STEP_SIZE);
		if (!bit_array)
        {
            tta_error (MEMORY_ERROR, NULL);
            return -1;
        }
	}
		
	*s &= bit_mask32[fbit];
	if (value < rbit) *s |= (bit_mask32[value]) << fbit;
	else {
		uint32_t unary = value;
		*s++ |= (bit_mask32[rbit]) << fbit;	unary -= rbit;
		for (;unary > 32; unary -= 32) *s++ = bit_mask32[32];
		if (unary) *s = bit_mask32[unary];
	}

	bit_array_bits += (value + 1);

    return 0;
}

 void
get_binary (uint32_t *value, uint32_t bits) {
	uint32_t fbit = bit_array_bits & 0x1FUL;
	uint32_t rbit = 32 - fbit;
	uint32_t pos = bit_array_bits >> 5;
	uint32_t *s = ((uint32_t *) bit_array) + pos;

	*value = 0;

	if (pos > bit_array_size) return;

	if (bits <= rbit)
		*value = (*s >> fbit) & bit_mask32[bits];
	else {
		*value = (*s++ >> fbit) & bit_mask32[rbit];
		*value |= (*s & bit_mask32[bits - rbit]) << rbit;
	}

	bit_array_bits += bits;
}

 void
get_unary (uint32_t *value) {
	uint32_t fbit = bit_array_bits & 0x1FUL;
	uint32_t rbit = 32 - fbit;
	uint32_t pos = bit_array_bits >> 5;
	uint32_t *s = ((uint32_t *) bit_array) + pos;
	uint32_t mask = 1;

	*value = 0;

	if (pos > bit_array_size) return;

	if ((*s >> fbit) == bit_mask32[rbit]) {
		*value += rbit; fbit = 0;
		while (*(++s) == bit_mask32[32]) *value += 32;
	}
	for (mask <<= fbit; *s & mask; mask <<= 1) (*value)++;

	bit_array_bits += (*value + 1);
}

void
encode_frame (int32_t *data, u_int32_t len) {
    u_int32_t value;
    u_int32_t unary, binary;
	int32_t *p, wk, k = 10;
	int32_t ksum = 16 << k;

	for (p = data; p < data + len; p++) {
		value = ENC(*p);

		wk = (k < 1) ? 0 : k - 1;
		ksum += value - (ksum >> 4);
		if (k > 0 && ksum < shift16[wk]) k--;
		else if (ksum > shift16[wk + 1]) k++;

		unary = value >> wk;
		binary = value & bit_mask32[wk]; 

		put_unary (unary);
		if (wk) put_binary (binary, wk);
	}
}

void
decode_frame (int32_t *data, uint32_t len) {
	uint32_t unary, binary;
	int32_t *p, wk, k = 10;
	int32_t ksum = 16 << k;
	int32_t value;

	for (p = data; p < data + len; p++) {

		wk = (k < 1) ? 0 : k - 1;

		get_unary (&unary);
		if (wk) {
			get_binary (&binary, wk);
			value = (unary << wk) + binary;
		} else value = unary;

		ksum += value - (ksum >> 4);
		if (k > 0 && ksum < shift16[wk]) k--;
		else if (ksum > shift16[wk + 1]) k++;

		*p = DEC(value);
	}
}

/* eof */

