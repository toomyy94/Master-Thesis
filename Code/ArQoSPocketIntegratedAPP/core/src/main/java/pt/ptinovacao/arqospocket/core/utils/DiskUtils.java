package pt.ptinovacao.arqospocket.core.utils;

import android.os.Environment;
import android.os.StatFs;

/**
 * Created by pedro on 24/04/2017.
 */
public class DiskUtils {

    private static final long MEGA_BYTE = 1048576;

    /**
     * Calculates total space on disk
     *
     * @param external If true will query external disk, otherwise will query internal disk.
     * @return Number of mega bytes on disk.
     */
    public static long totalSpace(boolean external) {
        StatFs statFs = getStats(external);
        long total;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            total = (statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / MEGA_BYTE;
        } else {
            //noinspection deprecation
            total = (((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize())) / MEGA_BYTE;
        }
        return total;
    }

    /**
     * Calculates free space on disk
     *
     * @param external If true will query external disk, otherwise will query internal disk.
     * @return Number of free mega bytes on disk.
     */
    public static int freeSpace(boolean external) {
        StatFs statFs = getStats(external);
        long availableBlocks;
        long blockSize;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        } else {
            //noinspection deprecation
            blockSize = statFs.getBlockSize();
            //noinspection deprecation
            availableBlocks = statFs.getAvailableBlocks();
        }
        long freeBytes = availableBlocks * blockSize;

        return (int) (freeBytes / MEGA_BYTE);
    }

    public static long percentageFreeSpace() {
        return ((DiskUtils.freeSpace(true) * 100) / DiskUtils.totalSpace(true));
    }

    private static StatFs getStats(boolean external) {
        String path;
        if (external) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            path = Environment.getRootDirectory().getAbsolutePath();
        }
        return new StatFs(path);
    }
}