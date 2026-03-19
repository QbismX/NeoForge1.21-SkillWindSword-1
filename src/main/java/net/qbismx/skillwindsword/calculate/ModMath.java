package net.qbismx.skillwindsword.calculate;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ModMath {

    private static final double EPS = 1e-12;

    public static boolean intersectRectAABB(Vec3 p0,Vec3 p1,Vec3 p2, Vec3 p3, AABB box) {
        Vec3 c = box.getCenter();
        double hx = (box.maxX - box.minX) * 0.5;
        double hy = (box.maxY - box.minY) * 0.5;
        double hz = (box.maxZ - box.minZ) * 0.5;

        // rectをAABBの中心基準に移動
        Vec3 q0 = p0.subtract(c);
        Vec3 q1 = p1.subtract(c);
        Vec3 q2 = p2.subtract(c);
        Vec3 q3 = p3.subtract(c);

        // AABB軸での判定
        if (!overlapAxisX(q0, q1, q2, q3, hx)) return false;
        if (!overlapAxisY(q0, q1, q2, q3, hy)) return false;
        if (!overlapAxisZ(q0, q1, q2, q3, hz)) return false;

        // 長方形の2辺
        Vec3 e1 = q1.subtract(q0);
        Vec3 e2 = q3.subtract(q0);

        // 長方形の法線
        Vec3 normal = e1.cross(e2);
        if (!overlapAxis(normal, q0, q1, q2, q3, hx, hy, hz)) return false;

        // その他視点
        if (!axisTest(0, e1.z, -e1.y, q0, q1, q2, q3, hx, hy, hz)) return false;

        if (!axisTest(-e1.z, 0, e1.x, q0, q1, q2, q3, hx, hy, hz)) return false;

        if (!axisTest(e1.y, -e1.x, 0, q0, q1, q2, q3, hx, hy, hz)) return false;

        if (!axisTest(0, e2.z, -e2.y, q0, q1, q2, q3, hx, hy, hz)) return false;

        if (!axisTest(-e2.z, 0, e2.x, q0, q1, q2, q3, hx, hy, hz)) return false;

        return axisTest(e2.y, -e2.x, 0, q0, q1, q2, q3, hx, hy, hz); // 全視点で重なる = ぶつかっている。
    }

    // 各視点での重なり判定　(XY面・YZ面・ZX面専用)
    private static boolean overlapAxisX(Vec3 p0,Vec3 p1,Vec3 p2, Vec3 p3, double hx) {
        double min = p0.x, max = p0.x;

        if (p1.x < min) min = p1.x; if (p1.x > max) max = p1.x;
        if (p2.x < min) min = p2.x; if (p2.x > max) max = p2.x;
        if (p3.x < min) min = p3.x; if (p3.x > max) max = p3.x;

        return !(max < -hx || min > hx);
    }

    private static boolean overlapAxisY(Vec3 p0,Vec3 p1,Vec3 p2, Vec3 p3, double hy) {
        double min = p0.y, max = p0.y;

        if (p1.y < min) min = p1.y; if (p1.y > max) max = p1.y;
        if (p2.y < min) min = p2.y; if (p2.y > max) max = p2.y;
        if (p3.y < min) min = p3.y; if (p3.y > max) max = p3.y;

        return !(max < -hy || min > hy);
    }
    private static boolean overlapAxisZ(Vec3 p0,Vec3 p1,Vec3 p2, Vec3 p3, double hz) {
        double min = p0.z, max = p0.z;

        if (p1.z < min) min = p1.z; if (p1.z > max) max = p1.z;
        if (p2.z < min) min = p2.z; if (p2.z > max) max = p2.z;
        if (p3.z < min) min = p3.z; if (p3.z > max) max = p3.z;

        return !(max < -hz || min > hz);
    }


    // 長方形法線視点専用
    private static boolean overlapAxis(Vec3 axis, Vec3 p0,Vec3 p1,Vec3 p2, Vec3 p3, double hx, double hy, double hz) {

        double ax = Math.abs(axis.x);
        double ay = Math.abs(axis.y);
        double az = Math.abs(axis.z);

        if (ax + ay + az < EPS) return true;

        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        double proj = p0.x * axis.x + p0.y * axis.y + p0.z * axis.z;
            if (proj < min) min = proj;
            if (proj > max) max = proj;

        proj = p1.x * axis.x + p1.y * axis.y + p1.z * axis.z;
            if (proj < min) min = proj;
            if (proj > max) max = proj;

        proj = p2.x * axis.x + p2.y * axis.y + p2.z * axis.z;
            if (proj < min) min = proj;
            if (proj > max) max = proj;

        proj = p3.x * axis.x + p3.y * axis.y + p3.z * axis.z;
            if (proj < min) min = proj;
            if (proj > max) max = proj;


        double r = hx * ax + hy * ay + hz * az;

        return !(max < -r || min > r);
    }

    // その他視点(6つ)
    private static boolean axisTest(double ax, double ay, double az,
                                    Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3,
                                    double hx, double hy, double hz) {

        double absX = Math.abs(ax);
        double absY = Math.abs(ay);
        double absZ = Math.abs(az);

        if (absX + absY + absZ < 1e-12) return true;

        double p0v = p0.x*ax + p0.y*ay + p0.z*az;
        double p1v = p1.x*ax + p1.y*ay + p1.z*az;
        double p2v = p2.x*ax + p2.y*ay + p2.z*az;
        double p3v = p3.x*ax + p3.y*ay + p3.z*az;

        double min = Math.min(Math.min(p0v, p1v), Math.min(p2v, p3v));
        double max = Math.max(Math.max(p0v, p1v), Math.max(p2v, p3v));

        double r = hx*absX + hy*absY + hz*absZ;

        return !(max < -r || min > r);
    }

}
