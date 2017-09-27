/*
 * entropy.h
 *
 * Description: Entropy coding definitions and prototypes
 * Developed by: Aleksander Djuric <ald@iszf.irk.ru>
 *               Pavel Zhilin <pzh@iszf.irk.ru>
 *
 * Copyright (c) 1999-2003 Aleksander Djuric. All rights reserved.
 * Distributed under the GNU Lesser General Public License (LGPL).
 * The complete text of the license can be found in the COPYING
 * file included in the distribution.
 *
 */

#ifndef ENTROPY_H
#define ENTROPY_H

#define BASE_SIZE 1024*1024
#define STEP_SIZE 1024*1024

#define ENC(x)  (((x)>0)?((x)<<1)-1:(-(x)<<1))
#define DEC(x)  (((x)&1)?(++(x)>>1):(-(x)>>1))

extern unsigned char *bit_array;

int init_bit_array_write(void);
int init_bit_array_read(uint32_t size);
int32_t get_len (void);

void encode_frame (int32_t *data, uint32_t len);
void decode_frame (int32_t *data, uint32_t len);

#endif	/* ENTROPY_H */
