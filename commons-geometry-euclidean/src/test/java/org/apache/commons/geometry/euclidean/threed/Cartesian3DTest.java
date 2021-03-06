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
import org.junit.Assert;
import org.junit.Test;

public class Cartesian3DTest {

    private static final double TEST_TOLERANCE = 1e-15;

    @Test
    public void testCoordinates() {
        // arrange
        Cartesian3D c = new StubCartesian3D(1, 2, 3);

        // act/assert
        Assert.assertEquals(1.0, c.getX(), TEST_TOLERANCE);
        Assert.assertEquals(2.0, c.getY(), TEST_TOLERANCE);
        Assert.assertEquals(3.0, c.getZ(), TEST_TOLERANCE);
    }

    @Test
    public void testToArray() {
        // arrange
        Cartesian3D c = new StubCartesian3D(1, 2, 3);

        // act
        double[] arr = c.toArray();

        // assert
        Assert.assertEquals(3, arr.length);
        Assert.assertEquals(1.0, arr[0], TEST_TOLERANCE);
        Assert.assertEquals(2.0, arr[1], TEST_TOLERANCE);
        Assert.assertEquals(3.0, arr[2], TEST_TOLERANCE);
    }


    @Test
    public void testToSpherical() {
        // arrange
        double sqrt3 = Math.sqrt(3);

        // act/assert
        checkSpherical(new StubCartesian3D(0, 0, 0).toSpherical(), 0, 0, 0);

        checkSpherical(new StubCartesian3D(0.1, 0, 0).toSpherical(), 0.1, 0, Geometry.HALF_PI);
        checkSpherical(new StubCartesian3D(-0.1, 0, 0).toSpherical(), 0.1, Geometry.PI, Geometry.HALF_PI);

        checkSpherical(new StubCartesian3D(0, 0.1, 0).toSpherical(), 0.1, Geometry.HALF_PI, Geometry.HALF_PI);
        checkSpherical(new StubCartesian3D(0, -0.1, 0).toSpherical(), 0.1, Geometry.PI + Geometry.HALF_PI, Geometry.HALF_PI);

        checkSpherical(new StubCartesian3D(0, 0, 0.1).toSpherical(), 0.1, 0, 0);
        checkSpherical(new StubCartesian3D(0, 0, -0.1).toSpherical(), 0.1, 0, Geometry.PI);

        checkSpherical(new StubCartesian3D(1, 1, 1).toSpherical(), sqrt3, 0.25 * Geometry.PI, Math.acos(1 / sqrt3));
        checkSpherical(new StubCartesian3D(-1, -1, -1).toSpherical(), sqrt3, 1.25 * Geometry.PI, Math.acos(-1 / sqrt3));
    }

    @Test
    public void testDimension() {
        // arrange
        Cartesian3D c = new StubCartesian3D(1, 2, 3);

        // act/assert
        Assert.assertEquals(3, c.getDimension());
    }

    @Test
    public void testNaN() {
        // act/assert
        Assert.assertTrue(new StubCartesian3D(0, 0, Double.NaN).isNaN());
        Assert.assertTrue(new StubCartesian3D(0, Double.NaN, 0).isNaN());
        Assert.assertTrue(new StubCartesian3D(Double.NaN, 0, 0).isNaN());

        Assert.assertFalse(new StubCartesian3D(1, 1, 1).isNaN());
        Assert.assertFalse(new StubCartesian3D(1, 1, Double.NEGATIVE_INFINITY).isNaN());
        Assert.assertFalse(new StubCartesian3D(1, Double.POSITIVE_INFINITY, 1).isNaN());
        Assert.assertFalse(new StubCartesian3D(Double.NEGATIVE_INFINITY, 1, 1).isNaN());
    }

    @Test
    public void testInfinite() {
        // act/assert
        Assert.assertTrue(new StubCartesian3D(0, 0, Double.NEGATIVE_INFINITY).isInfinite());
        Assert.assertTrue(new StubCartesian3D(0, Double.NEGATIVE_INFINITY, 0).isInfinite());
        Assert.assertTrue(new StubCartesian3D(Double.NEGATIVE_INFINITY, 0, 0).isInfinite());
        Assert.assertTrue(new StubCartesian3D(0, 0, Double.POSITIVE_INFINITY).isInfinite());
        Assert.assertTrue(new StubCartesian3D(0, Double.POSITIVE_INFINITY, 0).isInfinite());
        Assert.assertTrue(new StubCartesian3D(Double.POSITIVE_INFINITY, 0, 0).isInfinite());

        Assert.assertFalse(new StubCartesian3D(1, 1, 1).isInfinite());
        Assert.assertFalse(new StubCartesian3D(0, 0, Double.NaN).isInfinite());
        Assert.assertFalse(new StubCartesian3D(0, Double.NEGATIVE_INFINITY, Double.NaN).isInfinite());
        Assert.assertFalse(new StubCartesian3D(Double.NaN, 0, Double.NEGATIVE_INFINITY).isInfinite());
        Assert.assertFalse(new StubCartesian3D(Double.POSITIVE_INFINITY, Double.NaN, 0).isInfinite());
        Assert.assertFalse(new StubCartesian3D(0, Double.NaN, Double.POSITIVE_INFINITY).isInfinite());
    }

    @Test
    public void testToString() {
        // arrange
        StubCartesian3D c = new StubCartesian3D(1, 2, 3);
        Pattern pattern = Pattern.compile("\\(1.{0,2}, 2.{0,2}, 3.{0,2}\\)");

        // act
        String str = c.toString();

        // assert
        Assert.assertTrue("Expected string " + str + " to match regex " + pattern,
                    pattern.matcher(str).matches());
    }

    private void checkSpherical(SphericalCoordinates c, double radius, double azimuth, double polar) {
        Assert.assertEquals(radius, c.getRadius(), TEST_TOLERANCE);
        Assert.assertEquals(azimuth, c.getAzimuth(), TEST_TOLERANCE);
        Assert.assertEquals(polar, c.getPolar(), TEST_TOLERANCE);
    }

    private static class StubCartesian3D extends Cartesian3D {
        private static final long serialVersionUID = 1L;

        public StubCartesian3D(double x, double y, double z) {
            super(x, y, z);
        }
    }
}
