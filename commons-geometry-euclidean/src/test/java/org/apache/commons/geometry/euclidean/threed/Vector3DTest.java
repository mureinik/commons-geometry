/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.geometry.euclidean.threed;

import java.util.regex.Pattern;

import org.apache.commons.geometry.core.Geometry;
import org.apache.commons.geometry.core.GeometryTestUtils;
import org.apache.commons.geometry.core.exception.IllegalNormException;
import org.apache.commons.geometry.euclidean.EuclideanTestUtils;
import org.apache.commons.numbers.core.Precision;
import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.junit.Assert;
import org.junit.Test;

public class Vector3DTest {

    private static final double EPS = 1e-15;

    @Test
    public void testConstants() {
        // act/assert
        checkVector(Vector3D.ZERO, 0, 0, 0);

        checkVector(Vector3D.PLUS_X, 1, 0, 0);
        checkVector(Vector3D.MINUS_X, -1, 0, 0);

        checkVector(Vector3D.PLUS_Y, 0, 1, 0);
        checkVector(Vector3D.MINUS_Y, 0, -1, 0);

        checkVector(Vector3D.PLUS_Z, 0, 0, 1);
        checkVector(Vector3D.MINUS_Z, 0, 0, -1);

        checkVector(Vector3D.NaN, Double.NaN, Double.NaN, Double.NaN);
        checkVector(Vector3D.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        checkVector(Vector3D.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testConstants_normalize() {
        // act/assert
        GeometryTestUtils.assertThrows(() -> Vector3D.ZERO.normalize(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NaN.normalize(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.POSITIVE_INFINITY.normalize(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NEGATIVE_INFINITY.normalize(),
                IllegalNormException.class);

        Assert.assertSame(Vector3D.PLUS_X.normalize(), Vector3D.PLUS_X);
        Assert.assertSame(Vector3D.MINUS_X.normalize(), Vector3D.MINUS_X);

        Assert.assertSame(Vector3D.PLUS_Y.normalize(), Vector3D.PLUS_Y);
        Assert.assertSame(Vector3D.MINUS_Y.normalize(), Vector3D.MINUS_Y);

        Assert.assertSame(Vector3D.PLUS_Z.normalize(), Vector3D.PLUS_Z);
        Assert.assertSame(Vector3D.MINUS_Z.normalize(), Vector3D.MINUS_Z);
    }

    @Test
    public void testZero() {
        // act
        Vector3D zero = Vector3D.of(1, 2, 3).getZero();

        // assert
        checkVector(zero, 0, 0, 0);
        Assert.assertEquals(0, zero.getNorm(), EPS);
    }

    @Test
    public void testAsPoint() {
        // act/assert
        checkPoint(Vector3D.of(1, 2, 3).asPoint(), 1, 2, 3);
        checkPoint(Vector3D.of(-1, -2, -3).asPoint(), -1, -2, -3);
        checkPoint(Vector3D.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).asPoint(),
                Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testNorm() {
        // act/assert
        Assert.assertEquals(0.0, Vector3D.ZERO.getNorm(), 0);
        Assert.assertEquals(Math.sqrt(29), Vector3D.of(2, 3, 4).getNorm(), EPS);
        Assert.assertEquals(Math.sqrt(29), Vector3D.of(-2, -3, -4).getNorm(), EPS);
    }

    @Test
    public void testNormSq() {
        // act/assert
        Assert.assertEquals(0.0, Vector3D.ZERO.getNormSq(), 0);
        Assert.assertEquals(29, Vector3D.of(2, 3, 4).getNormSq(), EPS);
        Assert.assertEquals(29, Vector3D.of(-2, -3, -4).getNormSq(), EPS);
    }

    @Test
    public void testWithNorm() {
        // arrange
        double x = 2;
        double y = 3;
        double z = 4;

        double len = Math.sqrt((x * x) + (y * y) + (z * z));

        double normX = x / len;
        double normY = y / len;
        double normZ = z / len;

        // act/assert
        checkVector(Vector3D.of(x, y, z).withNorm(0.0), 0.0, 0.0, 0.0);

        checkVector(Vector3D.of(x, y, z).withNorm(1.0), normX, normY, normZ);
        checkVector(Vector3D.of(x, y, -z).withNorm(1.0), normX, normY, -normZ);
        checkVector(Vector3D.of(x, -y, z).withNorm(1.0), normX, -normY, normZ);
        checkVector(Vector3D.of(x, -y, -z).withNorm(1.0), normX, -normY, -normZ);
        checkVector(Vector3D.of(-x, y, z).withNorm(1.0), -normX, normY, normZ);
        checkVector(Vector3D.of(-x, y, -z).withNorm(1.0), -normX, normY, -normZ);
        checkVector(Vector3D.of(-x, -y, z).withNorm(1.0), -normX, -normY, normZ);
        checkVector(Vector3D.of(-x, -y, -z).withNorm(1.0), -normX, -normY, -normZ);

        checkVector(Vector3D.of(x, y, z).withNorm(0.5), 0.5 * normX, 0.5 * normY, 0.5 * normZ);
        checkVector(Vector3D.of(x, y, z).withNorm(3), 3 * normX, 3 * normY, 3 * normZ);

        checkVector(Vector3D.of(x, y, z).withNorm(-0.5), -0.5 * normX, -0.5 * normY, -0.5 * normZ);
        checkVector(Vector3D.of(x, y, z).withNorm(-3), -3 * normX, -3 * normY, -3 * normZ);

        for (double mag = -10.0; mag <= 10.0; ++mag)
        {
            Assert.assertEquals(Math.abs(mag), Vector3D.of(x, y, z).withNorm(mag).getNorm(), EPS);
        }
    }

    @Test
    public void testWithNorm_illegalNorm() {
        // act/assert
        GeometryTestUtils.assertThrows(() -> Vector3D.ZERO.withNorm(2.0),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NaN.withNorm(2.0),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.POSITIVE_INFINITY.withNorm(2.0),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NEGATIVE_INFINITY.withNorm(2.0),
                IllegalNormException.class);
    }

    @Test
    public void testWithNorm_unitVectors() {
        // arrange
        Vector3D v = Vector3D.of(2.0, -3.0, 4.0).normalize();

        // act/assert
        checkVector(Vector3D.PLUS_X.withNorm(2.5), 2.5, 0.0, 0.0);
        checkVector(Vector3D.MINUS_Y.withNorm(3.14), 0.0, -3.14, 0.0);
        checkVector(Vector3D.PLUS_Z.withNorm(-1.1), 0.0, 0.0, -1.1);

        for (double mag = -10.0; mag <= 10.0; ++mag)
        {
            Assert.assertEquals(Math.abs(mag), v.withNorm(mag).getNorm(), EPS);
        }
    }

    @Test
    public void testAdd() {
        // arrange
        Vector3D v1 = Vector3D.of(1, 2, 3);
        Vector3D v2 = Vector3D.of(-4, -5, -6);
        Vector3D v3 = Vector3D.of(7, 8, 9);

        // act/assert
        checkVector(v1.add(v1), 2, 4, 6);

        checkVector(v1.add(v2), -3, -3, -3);
        checkVector(v2.add(v1), -3, -3, -3);

        checkVector(v1.add(v3), 8, 10, 12);
        checkVector(v3.add(v1), 8, 10, 12);
    }

    @Test
    public void testAdd_scaled() {
        // arrange
        Vector3D v1 = Vector3D.of(1, 2, 3);
        Vector3D v2 = Vector3D.of(-4, -5, -6);
        Vector3D v3 = Vector3D.of(7, 8, 9);

        // act/assert
        checkVector(v1.add(0, v1), 1, 2, 3);
        checkVector(v1.add(0.5, v1), 1.5, 3, 4.5);
        checkVector(v1.add(1, v1), 2, 4, 6);

        checkVector(v1.add(2, v2), -7, -8, -9);
        checkVector(v2.add(2, v1), -2, -1, -0);

        checkVector(v1.add(-2, v3), -13, -14, -15);
        checkVector(v3.add(-2, v1), 5, 4, 3);
    }

    @Test
    public void testSubtract() {
        // arrange
        Vector3D v1 = Vector3D.of(1, 2, 3);
        Vector3D v2 = Vector3D.of(-4, -5, -6);
        Vector3D v3 = Vector3D.of(7, 8, 9);

        // act/assert
        checkVector(v1.subtract(v1), 0, 0, 0);

        checkVector(v1.subtract(v2), 5, 7, 9);
        checkVector(v2.subtract(v1), -5, -7, -9);

        checkVector(v1.subtract(v3), -6, -6, -6);
        checkVector(v3.subtract(v1), 6, 6, 6);
    }

    @Test
    public void testSubtract_scaled() {
        // arrange
        Vector3D v1 = Vector3D.of(1, 2, 3);
        Vector3D v2 = Vector3D.of(-4, -5, -6);
        Vector3D v3 = Vector3D.of(7, 8, 9);

        // act/assert
        checkVector(v1.subtract(0, v1), 1, 2, 3);
        checkVector(v1.subtract(0.5, v1), 0.5, 1, 1.5);
        checkVector(v1.subtract(1, v1), 0, 0, 0);

        checkVector(v1.subtract(2, v2), 9, 12, 15);
        checkVector(v2.subtract(2, v1), -6, -9, -12);

        checkVector(v1.subtract(-2, v3), 15, 18, 21);
        checkVector(v3.subtract(-2, v1), 9, 12, 15);
    }

    @Test
    public void testNegate() {
        // act/assert
        checkVector(Vector3D.of(0.1, 2.5, 1.3).negate(), -0.1, -2.5, -1.3);
        checkVector(Vector3D.of(-0.1, -2.5, -1.3).negate(), 0.1, 2.5, 1.3);
    }

    @Test
    public void testNormalize() {
        // arrange
        double invSqrt3 = 1 / Math.sqrt(3);

        // act/assert
        checkVector(Vector3D.of(100, 0, 0).normalize(), 1, 0, 0);
        checkVector(Vector3D.of(-100, 0, 0).normalize(), -1, 0, 0);

        checkVector(Vector3D.of(0, 100, 0).normalize(), 0, 1, 0);
        checkVector(Vector3D.of(0, -100, 0).normalize(), 0, -1, 0);

        checkVector(Vector3D.of(0, 0, 100).normalize(), 0, 0, 1);
        checkVector(Vector3D.of(0, 0, -100).normalize(), 0, 0, -1);

        checkVector(Vector3D.of(2, 2, 2).normalize(), invSqrt3, invSqrt3, invSqrt3);
        checkVector(Vector3D.of(-2, -2, -2).normalize(), -invSqrt3, -invSqrt3, -invSqrt3);

        Assert.assertEquals(1.0, Vector3D.of(5, -4, 2).normalize().getNorm(), EPS);
    }

    @Test
    public void testNormalize_illegalNorm() {
        // act/assert
        GeometryTestUtils.assertThrows(() -> Vector3D.ZERO.normalize(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NaN.normalize(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.POSITIVE_INFINITY.normalize(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NEGATIVE_INFINITY.normalize(),
                IllegalNormException.class);
    }

    @Test
    public void testNormalize_isIdempotent() {
        // arrange
        double invSqrt3 = 1 / Math.sqrt(3);
        Vector3D v = Vector3D.of(2, 2, 2).normalize();

        // act/assert
        Assert.assertSame(v, v.normalize());
        checkVector(v.normalize(), invSqrt3, invSqrt3, invSqrt3);
    }

    @Test
    public void testOrthogonal() {
        // arrange
        Vector3D v1 = Vector3D.of(0.1, 2.5, 1.3);
        Vector3D v2 = Vector3D.of(2.3, -0.003, 7.6);
        Vector3D v3 = Vector3D.of(-1.7, 1.4, 0.2);
        Vector3D v4 = Vector3D.of(4.2, 0.1, -1.8);

        // act/assert
        Assert.assertEquals(0.0, v1.dotProduct(v1.orthogonal()), EPS);
        Assert.assertEquals(0.0, v2.dotProduct(v2.orthogonal()), EPS);
        Assert.assertEquals(0.0, v3.dotProduct(v3.orthogonal()), EPS);
        Assert.assertEquals(0.0, v4.dotProduct(v4.orthogonal()), EPS);
    }

    @Test
    public void testOrthogonal_illegalNorm() {
        // act/assert
        GeometryTestUtils.assertThrows(() -> Vector3D.ZERO.orthogonal(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NaN.orthogonal(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.POSITIVE_INFINITY.orthogonal(),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NEGATIVE_INFINITY.orthogonal(),
                IllegalNormException.class);
    }

    @Test
    public void testOrthogonal_givenDirection() {
        // arrange
        double invSqrt2 = 1.0 / Math.sqrt(2.0);

        // act/assert
        checkVector(Vector3D.PLUS_X.orthogonal(Vector3D.of(-1.0, 0.1, 0.0)), 0.0, 1.0, 0.0);
        checkVector(Vector3D.PLUS_Y.orthogonal(Vector3D.of(2.0, 2.0, 2.0)), invSqrt2, 0.0, invSqrt2);
        checkVector(Vector3D.PLUS_Z.orthogonal(Vector3D.of(3.0, 3.0, -3.0)), invSqrt2, invSqrt2, 0.0);

        checkVector(Vector3D.of(invSqrt2, invSqrt2, 0.0).orthogonal(Vector3D.of(1.0, 1.0, 0.2)), 0.0, 0.0, 1.0);
    }

    @Test
    public void testOrthogonal_givenDirection_illegalNorm() {
        // act/assert
        GeometryTestUtils.assertThrows(() -> Vector3D.ZERO.orthogonal(Vector3D.PLUS_X),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NaN.orthogonal(Vector3D.PLUS_X),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.POSITIVE_INFINITY.orthogonal(Vector3D.PLUS_X),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NEGATIVE_INFINITY.orthogonal(Vector3D.PLUS_X),
                IllegalNormException.class);

        GeometryTestUtils.assertThrows(() -> Vector3D.PLUS_X.orthogonal(Vector3D.ZERO),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.PLUS_X.orthogonal(Vector3D.NaN),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.PLUS_X.orthogonal(Vector3D.POSITIVE_INFINITY),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.PLUS_X.orthogonal(Vector3D.NEGATIVE_INFINITY),
                IllegalNormException.class);
    }

    @Test
    public void testOrthogonal_givenDirection_directionIsCollinear() {
        // act/assert
        GeometryTestUtils.assertThrows(() -> Vector3D.PLUS_X.orthogonal(Vector3D.PLUS_X),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.PLUS_X.orthogonal(Vector3D.MINUS_X),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.of(1.0, 1.0, 1.0).orthogonal(Vector3D.of(2.0, 2.0, 2.0)),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.of(-1.01, -1.01, -1.01).orthogonal(Vector3D.of(20.1, 20.1, 20.1)),
                IllegalNormException.class);
    }

    @Test
    public void testAngle() {
        // arrange
        double tolerance = 1e-10;

        Vector3D v1 = Vector3D.of(1, 2, 3);
        Vector3D v2 = Vector3D.of(4, 5, 6);

        // act/assert
        Assert.assertEquals(0.22572612855273393616, v1.angle(v2), tolerance);
        Assert.assertEquals(7.98595620686106654517199e-8, v1.angle(Vector3D.of(2, 4, 6.000001)), tolerance);
        Assert.assertEquals(3.14159257373023116985197793156, v1.angle(Vector3D.of(-2, -4, -6.000001)), tolerance);

        Assert.assertEquals(0.0, Vector3D.PLUS_X.angle(Vector3D.PLUS_X), tolerance);
        Assert.assertEquals(Geometry.PI, Vector3D.PLUS_X.angle(Vector3D.MINUS_X), tolerance);

        Assert.assertEquals(Geometry.HALF_PI, Vector3D.PLUS_X.angle(Vector3D.PLUS_Y), tolerance);
        Assert.assertEquals(Geometry.HALF_PI, Vector3D.PLUS_X.angle(Vector3D.MINUS_Y), tolerance);
        Assert.assertEquals(Geometry.HALF_PI, Vector3D.PLUS_X.angle(Vector3D.PLUS_Z), tolerance);
        Assert.assertEquals(Geometry.HALF_PI, Vector3D.PLUS_X.angle(Vector3D.MINUS_Z), tolerance);
    }

    @Test
    public void testAngle_illegalNorm() {
        // arrange
        Vector3D v = Vector3D.of(1.0, 1.0, 1.0);

        // act/assert
        GeometryTestUtils.assertThrows(() -> Vector3D.ZERO.angle(v),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NaN.angle(v),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.POSITIVE_INFINITY.angle(v),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.NEGATIVE_INFINITY.angle(v),
                IllegalNormException.class);

        GeometryTestUtils.assertThrows(() -> v.angle(Vector3D.ZERO),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.angle(Vector3D.NaN),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.angle(Vector3D.POSITIVE_INFINITY),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.angle(Vector3D.NEGATIVE_INFINITY),
                IllegalNormException.class);
    }

    @Test
    public void testAngle_angularSeparation() {
        // arrange
        Vector3D v1 = Vector3D.of(2, -1, 4);

        Vector3D  k = v1.normalize();
        Vector3D  i = k.orthogonal();
        Vector3D v2 = k.scalarMultiply(Math.cos(1.2)).add(i.scalarMultiply(Math.sin(1.2)));

        // act/assert
        Assert.assertTrue(Math.abs(v1.angle(v2) - 1.2) < 1.0e-12);
    }

    @Test
    public void testCrossProduct() {
        // act/assert
        checkVector(Vector3D.PLUS_X.crossProduct(Vector3D.PLUS_Y), 0, 0, 1);
        checkVector(Vector3D.PLUS_X.crossProduct(Vector3D.MINUS_Y), 0, 0, -1);

        checkVector(Vector3D.MINUS_X.crossProduct(Vector3D.MINUS_Y), 0, 0, 1);
        checkVector(Vector3D.MINUS_X.crossProduct(Vector3D.PLUS_Y), 0, 0, -1);

        checkVector(Vector3D.of(2, 1, -4).crossProduct(Vector3D.of(3, 1, -1)), 3, -10, -1);

        double invSqrt6 = 1 / Math.sqrt(6);
        checkVector(Vector3D.of(1, 1, 1).crossProduct(Vector3D.of(-1, 0, 1)).normalize(), invSqrt6, - 2 * invSqrt6, invSqrt6);
    }

    @Test
    public void testCrossProduct_nearlyAntiParallel() {
        // the vectors u1 and u2 are nearly but not exactly anti-parallel
        // (7.31e-16 degrees from 180 degrees) naive cross product (i.e.
        // computing u1.x * u2.x + u1.y * u2.y + u1.z * u2.z
        // leads to a result of   [0.0009765, -0.0001220, -0.0039062],
        // instead of the correct [0.0006913, -0.0001254, -0.0007909]

        // arrange
        final Vector3D u1 = Vector3D.of(-1321008684645961.0 /   268435456.0,
                                         -5774608829631843.0 /   268435456.0,
                                         -7645843051051357.0 /  8589934592.0);
        final Vector3D u2 = Vector3D.of( 1796571811118507.0 /  2147483648.0,
                                          7853468008299307.0 /  2147483648.0,
                                          2599586637357461.0 / 17179869184.0);
        final Vector3D u3 = Vector3D.of(12753243807587107.0 / 18446744073709551616.0,
                                         -2313766922703915.0 / 18446744073709551616.0,
                                          -227970081415313.0 /   288230376151711744.0);

        // act
        Vector3D cNaive = Vector3D.of(u1.getY() * u2.getZ() - u1.getZ() * u2.getY(),
                                       u1.getZ() * u2.getX() - u1.getX() * u2.getZ(),
                                       u1.getX() * u2.getY() - u1.getY() * u2.getX());
        Vector3D cAccurate = u1.crossProduct(u2);

        // assert
        Assert.assertTrue(u3.distance(cNaive) > 2.9 * u3.getNorm());
        Assert.assertEquals(0.0, u3.distance(cAccurate), 1.0e-30 * cAccurate.getNorm());
    }

    @Test
    public void testCrossProduct_accuracy() {
        // we compare accurate versus naive cross product implementations
        // on regular vectors (i.e. not extreme cases like in the previous test)
        UniformRandomProvider random = RandomSource.create(RandomSource.WELL_1024_A, 885362227452043215l);
        for (int i = 0; i < 10000; ++i) {
            // arrange
            double ux = 10000 * random.nextDouble();
            double uy = 10000 * random.nextDouble();
            double uz = 10000 * random.nextDouble();
            double vx = 10000 * random.nextDouble();
            double vy = 10000 * random.nextDouble();
            double vz = 10000 * random.nextDouble();

            // act
            Vector3D cNaive = Vector3D.of(uy * vz - uz * vy, uz * vx - ux * vz, ux * vy - uy * vx);
            Vector3D cAccurate = Vector3D.of(ux, uy, uz).crossProduct(Vector3D.of(vx, vy, vz));

            // assert
            Assert.assertEquals(0.0, cAccurate.distance(cNaive), 6.0e-15 * cAccurate.getNorm());
        }
    }

    @Test
    public void testCrossProduct_cancellation() {
        // act/assert
        Vector3D v1 = Vector3D.of(9070467121.0, 4535233560.0, 1);
        Vector3D v2 = Vector3D.of(9070467123.0, 4535233561.0, 1);
        checkVector(v1.crossProduct(v2), -1, 2, 1);

        double scale    = Math.scalb(1.0, 100);
        Vector3D big1   = Vector3D.linearCombination(scale, v1);
        Vector3D small2 = Vector3D.linearCombination(1 / scale, v2);
        checkVector(big1.crossProduct(small2), -1, 2, 1);
    }

    @Test
    public void testScalarMultiply() {
        // arrange
        Vector3D v1 = Vector3D.of(2, 3, 4);
        Vector3D v2 = Vector3D.of(-2, -3, -4);

        // act/assert
        checkVector(v1.scalarMultiply(0), 0, 0, 0);
        checkVector(v1.scalarMultiply(0.5), 1, 1.5, 2);
        checkVector(v1.scalarMultiply(1), 2, 3, 4);
        checkVector(v1.scalarMultiply(2), 4, 6, 8);
        checkVector(v1.scalarMultiply(-2), -4, -6, -8);

        checkVector(v2.scalarMultiply(0), 0, 0, 0);
        checkVector(v2.scalarMultiply(0.5), -1, -1.5, -2);
        checkVector(v2.scalarMultiply(1), -2, -3, -4);
        checkVector(v2.scalarMultiply(2), -4, -6, -8);
        checkVector(v2.scalarMultiply(-2), 4, 6, 8);
    }

    @Test
    public void testDistance() {
        // arrange
        Vector3D v1 = Vector3D.of(1, -2, 3);
        Vector3D v2 = Vector3D.of(-4, 2, 0);
        Vector3D v3 = Vector3D.of(5, -6, -7);

        // act/assert
        Assert.assertEquals(0.0, v1.distance(v1), EPS);
        Assert.assertEquals(0.0, v2.distance(v2), EPS);

        Assert.assertEquals(Math.sqrt(50), v1.distance(v2), EPS);
        Assert.assertEquals(Math.sqrt(50), v2.distance(v1), EPS);

        Assert.assertEquals(v1.subtract(v2).getNorm(), v1.distance(v2), EPS);

        Assert.assertEquals(Math.sqrt(132), v1.distance(v3), EPS);
        Assert.assertEquals(Math.sqrt(132), v3.distance(v1), EPS);
    }

    @Test
    public void testDistanceSq() {
        // arrange
        Vector3D v1 = Vector3D.of(1, -2, 3);
        Vector3D v2 = Vector3D.of(-4, 2, 0);
        Vector3D v3 = Vector3D.of(5, -6, -7);

        // act/assert
        Assert.assertEquals(0.0, v1.distanceSq(v1), EPS);
        Assert.assertEquals(0.0, v2.distanceSq(v2), EPS);

        Assert.assertEquals(50, v1.distanceSq(v2), EPS);
        Assert.assertEquals(50, v2.distanceSq(v1), EPS);

        Assert.assertEquals(v1.subtract(v2).getNormSq(), v1.distanceSq(v2), EPS);

        Assert.assertEquals(132, v1.distanceSq(v3), EPS);
        Assert.assertEquals(132, v3.distanceSq(v1), EPS);
  }

    @Test
    public void testDotProduct() {
        // arrange
        Vector3D v1 = Vector3D.of(1, -2, 3);
        Vector3D v2 = Vector3D.of(-4, 5, -6);
        Vector3D v3 = Vector3D.of(7, 8, 9);

        // act/assert
        Assert.assertEquals(14, v1.dotProduct(v1), EPS);

        Assert.assertEquals(-32, v1.dotProduct(v2), EPS);
        Assert.assertEquals(-32, v2.dotProduct(v1), EPS);

        Assert.assertEquals(18, v1.dotProduct(v3), EPS);
        Assert.assertEquals(18, v3.dotProduct(v1), EPS);
    }

    @Test
    public void testDotProduct_nearlyOrthogonal() {
        // the following two vectors are nearly but not exactly orthogonal
        // naive dot product (i.e. computing u1.x * u2.x + u1.y * u2.y + u1.z * u2.z
        // leads to a result of 0.0, instead of the correct -1.855129...

        // arrange
        Vector3D u1 = Vector3D.of(-1321008684645961.0 /  268435456.0,
                                   -5774608829631843.0 /  268435456.0,
                                   -7645843051051357.0 / 8589934592.0);
        Vector3D u2 = Vector3D.of(-5712344449280879.0 /    2097152.0,
                                   -4550117129121957.0 /    2097152.0,
                                    8846951984510141.0 /     131072.0);

        // act
        double sNaive = u1.getX() * u2.getX() + u1.getY() * u2.getY() + u1.getZ() * u2.getZ();
        double sAccurate = u1.dotProduct(u2);

        // assert
        Assert.assertEquals(0.0, sNaive, 1.0e-30);
        Assert.assertEquals(-2088690039198397.0 / 1125899906842624.0, sAccurate, 1.0e-15);
    }

    @Test
    public void testDotProduct_accuracy() {
        // we compare accurate versus naive dot product implementations
        // on regular vectors (i.e. not extreme cases like in the previous test)
        UniformRandomProvider random = RandomSource.create(RandomSource.WELL_1024_A, 553267312521321237l);
        for (int i = 0; i < 10000; ++i) {
            // arrange
            double ux = 10000 * random.nextDouble();
            double uy = 10000 * random.nextDouble();
            double uz = 10000 * random.nextDouble();
            double vx = 10000 * random.nextDouble();
            double vy = 10000 * random.nextDouble();
            double vz = 10000 * random.nextDouble();

            // act
            double sNaive = ux * vx + uy * vy + uz * vz;
            double sAccurate = Vector3D.of(ux, uy, uz).dotProduct(Vector3D.of(vx, vy, vz));

            // assert
            Assert.assertEquals(sNaive, sAccurate, 2.5e-16 * sAccurate);
        }
    }

    @Test
    public void testProject() {
        // arrange
        Vector3D v1 = Vector3D.of(2.0, 3.0, 4.0);
        Vector3D v2 = Vector3D.of(-5.0, -6.0, -7.0);

        // act/assert
        checkVector(Vector3D.ZERO.project(Vector3D.PLUS_X), 0.0, 0.0, 0.0);

        checkVector(v1.project(Vector3D.PLUS_X), 2.0, 0.0, 0.0);
        checkVector(v1.project(Vector3D.MINUS_X), 2.0, 0.0, 0.0);
        checkVector(v1.project(Vector3D.PLUS_Y), 0.0, 3.0, 0.0);
        checkVector(v1.project(Vector3D.MINUS_Y), 0.0, 3.0, 0.0);
        checkVector(v1.project(Vector3D.PLUS_Z), 0.0, 0.0, 4.0);
        checkVector(v1.project(Vector3D.MINUS_Z), 0.0, 0.0, 4.0);

        checkVector(v2.project(Vector3D.PLUS_X), -5.0, 0.0, 0.0);
        checkVector(v2.project(Vector3D.MINUS_X), -5.0, 0.0, 0.0);
        checkVector(v2.project(Vector3D.PLUS_Y), 0.0, -6.0, 0.0);
        checkVector(v2.project(Vector3D.MINUS_Y), 0.0, -6.0, 0.0);
        checkVector(v2.project(Vector3D.PLUS_Z), 0.0, 0.0, -7.0);
        checkVector(v2.project(Vector3D.MINUS_Z), 0.0, 0.0, -7.0);

        checkVector(v1.project(Vector3D.of(1.0, 1.0, 1.0)), 3.0, 3.0, 3.0);
        checkVector(v1.project(Vector3D.of(-1.0, -1.0, -1.0)), 3.0, 3.0, 3.0);

        checkVector(v2.project(Vector3D.of(1.0, 1.0, 1.0)), -6.0, -6.0, -6.0);
        checkVector(v2.project(Vector3D.of(-1.0, -1.0, -1.0)), -6.0, -6.0, -6.0);
    }

    @Test
    public void testProject_baseHasIllegalNorm() {
        // arrange
        Vector3D v = Vector3D.of(1.0, 1.0, 1.0);

        // act/assert
        GeometryTestUtils.assertThrows(() -> v.project(Vector3D.ZERO),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.project(Vector3D.NaN),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.project(Vector3D.POSITIVE_INFINITY),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.project(Vector3D.NEGATIVE_INFINITY),
                IllegalNormException.class);
    }

    @Test
    public void testReject() {
        // arrange
        Vector3D v1 = Vector3D.of(2.0, 3.0, 4.0);
        Vector3D v2 = Vector3D.of(-5.0, -6.0, -7.0);

        // act/assert
        checkVector(Vector3D.ZERO.reject(Vector3D.PLUS_X), 0.0, 0.0, 0.0);

        checkVector(v1.reject(Vector3D.PLUS_X), 0.0, 3.0, 4.0);
        checkVector(v1.reject(Vector3D.MINUS_X), 0.0, 3.0, 4.0);
        checkVector(v1.reject(Vector3D.PLUS_Y), 2.0, 0.0, 4.0);
        checkVector(v1.reject(Vector3D.MINUS_Y), 2.0, 0.0, 4.0);
        checkVector(v1.reject(Vector3D.PLUS_Z), 2.0, 3.0, 0.0);
        checkVector(v1.reject(Vector3D.MINUS_Z), 2.0, 3.0, 0.0);

        checkVector(v2.reject(Vector3D.PLUS_X), 0.0, -6.0, -7.0);
        checkVector(v2.reject(Vector3D.MINUS_X), 0.0, -6.0, -7.0);
        checkVector(v2.reject(Vector3D.PLUS_Y), -5.0, 0.0, -7.0);
        checkVector(v2.reject(Vector3D.MINUS_Y), -5.0, 0.0, -7.0);
        checkVector(v2.reject(Vector3D.PLUS_Z), -5.0, -6.0, 0.0);
        checkVector(v2.reject(Vector3D.MINUS_Z), -5.0, -6.0, 0.0);

        checkVector(v1.reject(Vector3D.of(1.0, 1.0, 1.0)), -1.0, 0.0, 1.0);
        checkVector(v1.reject(Vector3D.of(-1.0, -1.0, -1.0)), -1.0, 0.0, 1.0);

        checkVector(v2.reject(Vector3D.of(1.0, 1.0, 1.0)), 1.0, 0.0, -1.0);
        checkVector(v2.reject(Vector3D.of(-1.0, -1.0, -1.0)), 1.0, 0.0, -1.0);
    }

    @Test
    public void testReject_baseHasIllegalNorm() {
        // arrange
        Vector3D v = Vector3D.of(1.0, 1.0, 1.0);

        // act/assert
        GeometryTestUtils.assertThrows(() -> v.reject(Vector3D.ZERO),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.reject(Vector3D.NaN),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.reject(Vector3D.POSITIVE_INFINITY),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> v.reject(Vector3D.NEGATIVE_INFINITY),
                IllegalNormException.class);
    }

    @Test
    public void testProjectAndReject_areComplementary() {
        // arrange
        double eps = 1e-12;

        // act/assert
        checkProjectAndRejectFullSphere(Vector3D.of(1.0, 0.0, 0.0), 1.0, eps);
        checkProjectAndRejectFullSphere(Vector3D.of(0.0, 1.0, 0.0), 2.0, eps);
        checkProjectAndRejectFullSphere(Vector3D.of(0.0, 0.0, 1.0), 2.0, eps);
        checkProjectAndRejectFullSphere(Vector3D.of(1.0, 1.0, 1.0), 3.0, eps);

        checkProjectAndRejectFullSphere(Vector3D.of(-2.0, 0.0, 0.0), 1.0, eps);
        checkProjectAndRejectFullSphere(Vector3D.of(0.0, -2.0, 0.0), 2.0, eps);
        checkProjectAndRejectFullSphere(Vector3D.of(0.0, 0.0, -2.0), 2.0, eps);
        checkProjectAndRejectFullSphere(Vector3D.of(-2.0, -2.0, -2.0), 3.0, eps);
    }

    private void checkProjectAndRejectFullSphere(Vector3D vec, double baseMag, double eps) {
        for (double polar = 0.0; polar <= Geometry.PI; polar += 0.5) {
            for (double azimuth = 0.0; azimuth <= Geometry.TWO_PI; azimuth += 0.5) {
                Vector3D base = Vector3D.ofSpherical(baseMag, azimuth, polar);

                Vector3D proj = vec.project(base);
                Vector3D rej = vec.reject(base);

                // ensure that the projection and rejection sum to the original vector
                EuclideanTestUtils.assertCoordinatesEqual(vec, proj.add(rej), eps);

                double angle = base.angle(vec);

                // check the angle between the projection and the base; this will
                // be undefined when the angle between the original vector and the
                // base is pi/2 (which means that the projection is the zero vector)
                if (angle < Geometry.HALF_PI) {
                    Assert.assertEquals(0.0, proj.angle(base), eps);
                }
                else if (angle > Geometry.HALF_PI) {
                    Assert.assertEquals(Geometry.PI, proj.angle(base), eps);
                }

                // check the angle between the rejection and the base; this should
                // always be pi/2 except for when the angle between the original vector
                // and the base is 0 or pi, in which case the rejection is the zero vector.
                if (angle > 0.0 && angle < Geometry.PI) {
                    Assert.assertEquals(Geometry.HALF_PI, rej.angle(base), eps);
                }
            }
        }
    }

    @Test
    public void testLerp() {
        // arrange
        Vector3D v1 = Vector3D.of(1, -5, 2);
        Vector3D v2 = Vector3D.of(-4, 0, 2);
        Vector3D v3 = Vector3D.of(10, -4, 0);

        // act/assert
        checkVector(v1.lerp(v1, 0), 1, -5, 2);
        checkVector(v1.lerp(v1, 1), 1, -5, 2);

        checkVector(v1.lerp(v2, -0.25), 2.25, -6.25, 2);
        checkVector(v1.lerp(v2, 0), 1, -5, 2);
        checkVector(v1.lerp(v2, 0.25), -0.25, -3.75, 2);
        checkVector(v1.lerp(v2, 0.5), -1.5, -2.5, 2);
        checkVector(v1.lerp(v2, 0.75), -2.75, -1.25, 2);
        checkVector(v1.lerp(v2, 1), -4, 0, 2);
        checkVector(v1.lerp(v2, 1.25), -5.25, 1.25, 2);

        checkVector(v1.lerp(v3, 0), 1, -5, 2);
        checkVector(v1.lerp(v3, 0.25), 3.25, -4.75, 1.5);
        checkVector(v1.lerp(v3, 0.5), 5.5, -4.5, 1);
        checkVector(v1.lerp(v3, 0.75), 7.75, -4.25, 0.5);
        checkVector(v1.lerp(v3, 1), 10, -4, 0);
    }

    @Test
    public void testHashCode() {
        // arrange
        double delta = 10 * Precision.EPSILON;
        Vector3D u = Vector3D.of(1, 1, 1);
        Vector3D v = Vector3D.of(1 + delta, 1 + delta, 1 + delta);
        Vector3D w = Vector3D.of(1, 1, 1);

        // act/assert
        Assert.assertTrue(u.hashCode() != v.hashCode());
        Assert.assertEquals(u.hashCode(), w.hashCode());

        Assert.assertEquals(Vector3D.of(0, 0, Double.NaN).hashCode(), Vector3D.NaN.hashCode());
        Assert.assertEquals(Vector3D.of(0, Double.NaN, 0).hashCode(), Vector3D.NaN.hashCode());
        Assert.assertEquals(Vector3D.of(Double.NaN, 0, 0).hashCode(), Vector3D.NaN.hashCode());
        Assert.assertEquals(Vector3D.of(0, 0, Double.NaN).hashCode(), Vector3D.of(Double.NaN, 0, 0).hashCode());
    }

    @Test
    public void testEquals() {
        // arrange
        double delta = 10 * Precision.EPSILON;
        Vector3D u1 = Vector3D.of(1, 2, 3);
        Vector3D u2 = Vector3D.of(1, 2, 3);

        // act/assert
        Assert.assertFalse(u1.equals(null));
        Assert.assertFalse(u1.equals(new Object()));

        Assert.assertTrue(u1.equals(u1));
        Assert.assertTrue(u1.equals(u2));

        Assert.assertFalse(u1.equals(Vector3D.of(-1, -2, -3)));
        Assert.assertFalse(u1.equals(Vector3D.of(1 + delta, 2, 3)));
        Assert.assertFalse(u1.equals(Vector3D.of(1, 2 + delta, 3)));
        Assert.assertFalse(u1.equals(Vector3D.of(1, 2, 3 + delta)));

        Assert.assertTrue(Vector3D.of(0, Double.NaN, 0).equals(Vector3D.of(Double.NaN, 0, 0)));

        Assert.assertTrue(Vector3D.of(0, 0, Double.POSITIVE_INFINITY).equals(Vector3D.of(0, 0, Double.POSITIVE_INFINITY)));
        Assert.assertFalse(Vector3D.of(0, Double.POSITIVE_INFINITY, 0).equals(Vector3D.of(0, 0, Double.POSITIVE_INFINITY)));
        Assert.assertFalse(Vector3D.of(Double.POSITIVE_INFINITY, 0, 0).equals(Vector3D.of(0, 0, Double.POSITIVE_INFINITY)));

        Assert.assertTrue(Vector3D.of(Double.NEGATIVE_INFINITY, 0, 0).equals(Vector3D.of(Double.NEGATIVE_INFINITY, 0, 0)));
        Assert.assertFalse(Vector3D.of(0, Double.NEGATIVE_INFINITY, 0).equals(Vector3D.of(Double.NEGATIVE_INFINITY, 0, 0)));
        Assert.assertFalse(Vector3D.of(0, 0, Double.NEGATIVE_INFINITY).equals(Vector3D.of(Double.NEGATIVE_INFINITY, 0, 0)));
    }

    @Test
    public void testToString() {
        // arrange
        Vector3D v = Vector3D.of(1, 2, 3);
        Pattern pattern = Pattern.compile("\\(1.{0,2}, 2.{0,2}, 3.{0,2}\\)");

        // act
        String str = v.toString();

        // assert
        Assert.assertTrue("Expected string " + str + " to match regex " + pattern,
                    pattern.matcher(str).matches());
    }

    @Test
    public void testParse() {
        // act/assert
        checkVector(Vector3D.parse("(1, 2, 3)"), 1, 2, 3);
        checkVector(Vector3D.parse("(-1, -2, -3)"), -1, -2, -3);

        checkVector(Vector3D.parse("(0.01, -1e-3, 0)"), 1e-2, -1e-3, 0);

        checkVector(Vector3D.parse("(NaN, -Infinity, Infinity)"), Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

        checkVector(Vector3D.parse(Vector3D.ZERO.toString()), 0, 0, 0);
        checkVector(Vector3D.parse(Vector3D.MINUS_X.toString()), -1, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_failure() {
        // act/assert
        Vector3D.parse("abc");
    }

    @Test
    public void testOf() {
        // act/assert
        checkVector(Vector3D.of(1, 2, 3), 1, 2, 3);
        checkVector(Vector3D.of(-1, -2, -3), -1, -2, -3);
        checkVector(Vector3D.of(Math.PI, Double.NaN, Double.POSITIVE_INFINITY),
                Math.PI, Double.NaN, Double.POSITIVE_INFINITY);
        checkVector(Vector3D.of(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Math.E),
                Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Math.E);
    }

    @Test
    public void testOfArray() {
        // act/assert
        checkVector(Vector3D.ofArray(new double[] { 1, 2, 3 }), 1, 2, 3);
        checkVector(Vector3D.ofArray(new double[] { -1, -2, -3 }), -1, -2, -3);
        checkVector(Vector3D.ofArray(new double[] { Math.PI, Double.NaN, Double.POSITIVE_INFINITY }),
                Math.PI, Double.NaN, Double.POSITIVE_INFINITY);
        checkVector(Vector3D.ofArray(new double[] { Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Math.E}),
                Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Math.E);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOfArray_invalidDimensions() {
        // act/assert
        Vector3D.ofArray(new double[] { 0.0, 0.0 });
    }

    @Test
    public void testOfSpherical() {
        // arrange
        double sqrt3 = Math.sqrt(3);

        // act/assert
        checkVector(Vector3D.ofSpherical(0, 0, 0), 0, 0, 0);

        checkVector(Vector3D.ofSpherical(1, 0, Geometry.HALF_PI), 1, 0, 0);
        checkVector(Vector3D.ofSpherical(1, Geometry.PI, Geometry.HALF_PI), -1, 0, 0);

        checkVector(Vector3D.ofSpherical(2, Geometry.HALF_PI, Geometry.HALF_PI), 0, 2, 0);
        checkVector(Vector3D.ofSpherical(2, Geometry.MINUS_HALF_PI, Geometry.HALF_PI), 0, -2, 0);

        checkVector(Vector3D.ofSpherical(3, 0, 0), 0, 0, 3);
        checkVector(Vector3D.ofSpherical(3, 0, Geometry.PI), 0, 0, -3);

        checkVector(Vector3D.ofSpherical(sqrt3, 0.25 * Geometry.PI, Math.acos(1 / sqrt3)), 1, 1, 1);
        checkVector(Vector3D.ofSpherical(sqrt3, -0.75 * Geometry.PI, Math.acos(-1 / sqrt3)), -1, -1, -1);
    }

    @Test
    public void testNormalize_static() {
        // arrange
        double invSqrt3 = 1.0 / Math.sqrt(3.0);

        // act/assert
        checkVector(Vector3D.normalize(2.0, -2.0, 2.0), invSqrt3, -invSqrt3, invSqrt3);
        checkVector(Vector3D.normalize(-4.0, 4.0, -4.0), -invSqrt3, invSqrt3, -invSqrt3);
    }

    @Test
    public void testNormalize_static_illegalNorm() {
        GeometryTestUtils.assertThrows(() -> Vector3D.normalize(0.0, 0.0, 0.0),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.normalize(Double.NaN, 1.0, 1.0),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.normalize(1.0, Double.NEGATIVE_INFINITY, 1.0),
                IllegalNormException.class);
        GeometryTestUtils.assertThrows(() -> Vector3D.normalize(1.0, 1.0, Double.POSITIVE_INFINITY),
                IllegalNormException.class);
    }

    @Test
    public void testLinearCombination1() {
        // arrange
        Vector3D p1 = Vector3D.of(1, 2, 3);

        // act/assert
        checkVector(Vector3D.linearCombination(0, p1), 0, 0, 0);

        checkVector(Vector3D.linearCombination(1, p1), 1, 2, 3);
        checkVector(Vector3D.linearCombination(-1, p1), -1, -2, -3);

        checkVector(Vector3D.linearCombination(0.5, p1), 0.5, 1, 1.5);
        checkVector(Vector3D.linearCombination(-0.5, p1), -0.5, -1, -1.5);
    }

    @Test
    public void testLinearCombination2() {
        // arrange
        Vector3D p1 = Vector3D.of(1, 2, 3);
        Vector3D p2 = Vector3D.of(-3, -4, -5);

        // act/assert
        checkVector(Vector3D.linearCombination(2, p1, -3, p2), 11, 16, 21);
        checkVector(Vector3D.linearCombination(-3, p1, 2, p2), -9, -14, -19);
    }

    @Test
    public void testLinearCombination3() {
        // arrange
        Vector3D p1 = Vector3D.of(1, 2, 3);
        Vector3D p2 = Vector3D.of(-3, -4, -5);
        Vector3D p3 = Vector3D.of(5, 6, 7);

        // act/assert
        checkVector(Vector3D.linearCombination(2, p1, -3, p2, 4, p3), 31, 40, 49);
        checkVector(Vector3D.linearCombination(-3, p1, 2, p2, -4, p3), -29, -38, -47);
    }

    @Test
    public void testLinearCombination4() {
        // arrange
        Vector3D p1 = Vector3D.of(1, 2, 3);
        Vector3D p2 = Vector3D.of(-3, -4, -5);
        Vector3D p3 = Vector3D.of(5, 6, 7);
        Vector3D p4 = Vector3D.of(-7, -8, 9);

        // act/assert
        checkVector(Vector3D.linearCombination(2, p1, -3, p2, 4, p3, -5, p4), 66, 80, 4);
        checkVector(Vector3D.linearCombination(-3, p1, 2, p2, -4, p3, 5, p4), -64, -78, -2);
    }

    private void checkVector(Vector3D v, double x, double y, double z) {
        Assert.assertEquals(x, v.getX(), EPS);
        Assert.assertEquals(y, v.getY(), EPS);
        Assert.assertEquals(z, v.getZ(), EPS);
    }

    private void checkPoint(Point3D p, double x, double y, double z) {
        Assert.assertEquals(x, p.getX(), EPS);
        Assert.assertEquals(y, p.getY(), EPS);
        Assert.assertEquals(z, p.getZ(), EPS);
    }
}
