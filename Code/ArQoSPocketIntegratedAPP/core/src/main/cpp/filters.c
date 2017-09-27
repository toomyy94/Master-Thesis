/*
 * filters.c
 *
 * Description: TTA filters.
 * Developed by: Aleksander Djuric <ald@iszf.irk.ru>
 *               Pavel Zhilin <pzh@iszf.irk.ru>
 *
 * Copyright (c) 1999-2003 Aleksander Djuric. All rights reserved.
 * Distributed under the GNU Lesser General Public License (LGPL).
 * The complete text of the license can be found in the COPYING
 * file included in the distribution.
 *
 */

#include <stdio.h>
#include <memory.h>
#include "filters.h"

typedef struct {
	int32_t order;
	int32_t mode;
	int32_t shift;
	int32_t round;
	int32_t qm[MAX_ORDER];
	int32_t dx[BUF_SIZE];
	int32_t dl[BUF_SIZE];
	int32_t *px;
	int32_t *pl;
} fltst;

static fltst fst1, fst2, fst3;

///////// Filters Settings /////////
static int flt_set [3][3][4][3] = {
	8,10,0,  8,9,0,   8,10,0,  8,12,1,
	8,10,0,  8,11,0,  8,10,0,  16,12,1,
	8,10,0,  8,11,0,  8,10,0,  32,12,1,

	0,0,0,   0,0,0,   0,0,0,   0,0,0,
	16,10,1, 16,9,1,  16,10,1, 0,0,0,
	32,11,1, 32,11,1, 32,11,1, 0,0,0,

	0,0,0,   0,0,0,   0,0,0,   0,0,0,
	0,0,0,   0,0,0,   0,0,0,   0,0,0,
	0,0,0,   16,9,1,  16,10,1, 0,0,0
};

 void
filter_compress (fltst *fs, int32_t *in) {
     int32_t *pA, *pB, *pE;
     int32_t out, sum;

	pA = fs->pl;
	pB = fs->qm;
	pE = pA + fs->order;

	for (sum = fs->round; pA < pE;
		pA += 8, pB += 8) {
		sum += *(pA+0) * *(pB+0);
		sum += *(pA+1) * *(pB+1);
		sum += *(pA+2) * *(pB+2);
		sum += *(pA+3) * *(pB+3);
		sum += *(pA+4) * *(pB+4);
		sum += *(pA+5) * *(pB+5);
		sum += *(pA+6) * *(pB+6);
		sum += *(pA+7) * *(pB+7);
	}

	out = *in - (sum >> fs->shift);
	
	pA = fs->pl + fs->order;
	*pA = *in;

	if (!fs->mode) {
		pB = fs->pl + fs->order - 1;

		// adaptive polynomial predictors
		*(pB-0) = *(pA-0) - *(pB-0);
		*(pB-1) = *(pA-1) - *(pB-1);
		*(pB-2) = *(pA-2) - *(pB-2);
	}

	pA = fs->qm;
	pB = fs->px;
	pE = pA + fs->order;

	if (out < 0)
		for (; pA < pE;
		pA += 8, pB += 8) {
		*(pA+0) += *(pB+0);
		*(pA+1) += *(pB+1);
		*(pA+2) += *(pB+2);
		*(pA+3) += *(pB+3);
		*(pA+4) += *(pB+4);
		*(pA+5) += *(pB+5);
		*(pA+6) += *(pB+6);
		*(pA+7) += *(pB+7);
	} else if (out > 0)
		for (; pA < pE;
		pA += 8, pB += 8) {
		*(pA+0) -= *(pB+0);
		*(pA+1) -= *(pB+1);
		*(pA+2) -= *(pB+2);
		*(pA+3) -= *(pB+3);
		*(pA+4) -= *(pB+4);
		*(pA+5) -= *(pB+5);
		*(pA+6) -= *(pB+6);
		*(pA+7) -= *(pB+7);
	}

	pA = fs->px + fs->order;
	pB = fs->pl + fs->order;

	*(pA-0) = ((*(pB-0) >> 28) & 8) - 4;
	*(pA-1) = ((*(pB-1) >> 29) & 4) - 2;
	*(pA-2) = ((*(pB-2) >> 29) & 4) - 2;
	*(pA-3) = ((*(pB-3) >> 30) & 2) - 1;

	if (fs->px + fs->order == fs->dx + (BUF_SIZE-1)) {
		memcpy(fs->dx, fs->px + 1, fs->order *
			sizeof(int32_t)); fs->px = fs->dx;
	} else 	fs->px++;

	if (fs->pl + fs->order ==  fs->dl + (BUF_SIZE-1)) {
		memcpy(fs->dl, fs->pl + 1, fs->order *
			sizeof(int32_t)); fs->pl = fs->dl;
	} else fs->pl++;
	
	*in = out;
}

 void
filter_decompress (fltst *fs, int32_t *in) {
     int32_t *pA, *pB, *pE;
     int32_t out, sum;

	pA = fs->pl;
	pB = fs->qm;
	pE = pA + fs->order;
	
	for (sum = fs->round;
		pA < pE; pA += 8, pB += 8) {
		sum += *(pA+0) * *(pB+0);
		sum += *(pA+1) * *(pB+1);
		sum += *(pA+2) * *(pB+2);
		sum += *(pA+3) * *(pB+3);
		sum += *(pA+4) * *(pB+4);
		sum += *(pA+5) * *(pB+5);
		sum += *(pA+6) * *(pB+6);
		sum += *(pA+7) * *(pB+7);
	}

	out = *in + (sum >> fs->shift);

	pA = fs->pl + fs->order;
	*pA = out;

	if (!fs->mode) {
		pB = fs->pl + fs->order - 1;

		// adaptive polynomial predictors
		*(pB-0) = *(pA-0) - *(pB-0);
		*(pB-1) = *(pA-1) - *(pB-1);
		*(pB-2) = *(pA-2) - *(pB-2);
	}

	pA = fs->qm;
	pB = fs->px;
	pE = pA + fs->order;

	if (*in < 0)
		for (; pA < pE;
		pA += 8, pB += 8) {
		*(pA+0) += *(pB+0);
		*(pA+1) += *(pB+1);
		*(pA+2) += *(pB+2);
		*(pA+3) += *(pB+3);
		*(pA+4) += *(pB+4);
		*(pA+5) += *(pB+5);
		*(pA+6) += *(pB+6);
		*(pA+7) += *(pB+7);
	} else if (*in > 0)
		for (; pA < pE;
		pA += 8, pB += 8) {
		*(pA+0) -= *(pB+0);
		*(pA+1) -= *(pB+1);
		*(pA+2) -= *(pB+2);
		*(pA+3) -= *(pB+3);
		*(pA+4) -= *(pB+4);
		*(pA+5) -= *(pB+5);
		*(pA+6) -= *(pB+6);
		*(pA+7) -= *(pB+7);
	}

	pA = fs->px + fs->order;
	pB = fs->pl + fs->order;

	*(pA-0) = ((*(pB-0) >> 28) & 8) - 4;
	*(pA-1) = ((*(pB-1) >> 29) & 4) - 2;
	*(pA-2) = ((*(pB-2) >> 29) & 4) - 2;
	*(pA-3) = ((*(pB-3) >> 30) & 2) - 1;

	if (fs->px + fs->order == fs->dx + (BUF_SIZE - 1)) {
		memcpy(fs->dx, fs->px + 1, fs->order *
			sizeof(int32_t)); fs->px = fs->dx;
	} else 	fs->px++;

	if (fs->pl + fs->order ==  fs->dl + (BUF_SIZE - 1)) {
		memcpy(fs->dl, fs->pl + 1, fs->order *
			sizeof(int32_t)); fs->pl = fs->dl;
	} else fs->pl++;
	
	*in = out;
}

static void
filter_init (fltst *fs, int32_t order, int32_t shift, int mode) {
	memset (fs, 0, sizeof(fltst));
	fs->px = fs->dx;
	fs->pl = fs->dl;
	fs->order = order;
	fs->shift = shift;
	fs->round = 1 << (shift - 1);
	fs->mode = mode;
}

void
filters_compress (int32_t *data, uint32_t len, int32_t level, int32_t byte_size) {
    int32_t *p = data;
    int32_t tmp, last;

    int32_t *f1 = flt_set[0][level-1][byte_size-1];
    int32_t *f2 = flt_set[1][level-1][byte_size-1];
    int32_t *f3 = flt_set[2][level-1][byte_size-1];

	filter_init (&fst1, f1[0], f1[1], f1[2]);
	filter_init (&fst2, f2[0], f2[1], f2[2]);
	filter_init (&fst3, f3[0], f3[1], f3[2]);

	for (last = 0; p < data + len; p++) {

		// compress stage 1: fixed order 1 prediction
		tmp = *p; switch (byte_size) {
		case 1: *p -= PREDICTOR1(last, 4); break;	// bps 8
		case 2: *p -= PREDICTOR1(last, 5); break;	// bps 16
		case 3: *p -= PREDICTOR1(last, 5); break;	// bps 24
		case 4: *p -= last; break;					// bps 32
		} last = tmp;

		// compress stage 2: adaptive hybrid filters
		if (fst1.order) filter_compress (&fst1, p);
		if (fst2.order) filter_compress (&fst2, p);
		if (fst3.order) filter_compress (&fst3, p);
	}
}

void
filters_decompress (int32_t *data, uint32_t len, int32_t level, int32_t byte_size) {
	int32_t *p = data;
	int32_t last;

	int32_t *f1 = flt_set[0][level-1][byte_size-1];
	int32_t *f2 = flt_set[1][level-1][byte_size-1];
	int32_t *f3 = flt_set[2][level-1][byte_size-1];

	filter_init (&fst1, f1[0], f1[1], f1[2]);
	filter_init (&fst2, f2[0], f2[1], f2[2]);
	filter_init (&fst3, f3[0], f3[1], f3[2]);

	for (last = 0; p < data + len; p++) {

		// decompress stage 1: adaptive hybrid filters
		if (fst3.order) filter_decompress (&fst3, p);
		if (fst2.order) filter_decompress (&fst2, p);
		if (fst1.order) filter_decompress (&fst1, p);

		// decompress stage 2: fixed order 1 prediction
		switch (byte_size) {
		case 1: *p += PREDICTOR1(last, 4); break;	// bps 8
		case 2: *p += PREDICTOR1(last, 5); break;	// bps 16
		case 3: *p += PREDICTOR1(last, 5); break;	// bps 24
		case 4: *p += last; break;					// bps 32
		} last = *p;
	}
}

/* eof */

