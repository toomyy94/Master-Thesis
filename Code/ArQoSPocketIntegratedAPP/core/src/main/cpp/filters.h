/*
 * filters.h
 *
 * Description: TTA filters definitions and prototypes
 * Developed by: Aleksander Djuric <ald@iszf.irk.ru>
 *               Pavel Zhilin <pzh@iszf.irk.ru>
 *
 * Copyright (c) 1999-2003 Aleksander Djuric. All rights reserved.
 * Distributed under the GNU Lesser General Public License (LGPL).
 * The complete text of the license can be found in the COPYING
 * file included in the distribution.
 *
 */

#ifndef FILTERS_H
#define FILTERS_H

#define MAX_ORDER	32
#define BUF_SIZE	4096

#ifndef M_LN2
#define	M_LN2		0.69314718055994530942
#endif

#ifdef _WIN32
	typedef unsigned __int64 uint64;
#else
	typedef unsigned long long uint64;
#endif

#define PREDICTOR1(x, k)	((long)((((uint64)x << k) - x) >> k))

void filters_compress (int32_t *data, uint32_t len, int32_t level, int32_t byte_size);
void filters_decompress (int32_t *data, uint32_t len, int32_t level, int32_t byte_size);

#endif	/* FILTERS_H */
