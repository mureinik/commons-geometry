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
package org.apache.commons.geometry.spherical.oned;

import org.apache.commons.geometry.core.partitioning.AbstractSubHyperplane;
import org.apache.commons.geometry.core.partitioning.Hyperplane;
import org.apache.commons.geometry.core.partitioning.Region;

/** This class represents sub-hyperplane for {@link LimitAngle}.
 * <p>Instances of this class are guaranteed to be immutable.</p>
 */
public class SubLimitAngle extends AbstractSubHyperplane<S1Point, S1Point> {

    /** Simple constructor.
     * @param hyperplane underlying hyperplane
     * @param remainingRegion remaining region of the hyperplane
     */
    public SubLimitAngle(final Hyperplane<S1Point> hyperplane,
                         final Region<S1Point> remainingRegion) {
        super(hyperplane, remainingRegion);
    }

    /** {@inheritDoc} */
    @Override
    public double getSize() {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected AbstractSubHyperplane<S1Point, S1Point> buildNew(final Hyperplane<S1Point> hyperplane,
                                                                 final Region<S1Point> remainingRegion) {
        return new SubLimitAngle(hyperplane, remainingRegion);
    }

    /** {@inheritDoc} */
    @Override
    public SplitSubHyperplane<S1Point> split(final Hyperplane<S1Point> hyperplane) {
        final double global = hyperplane.getOffset(((LimitAngle) getHyperplane()).getLocation());
        return (global < -1.0e-10) ?
                                    new SplitSubHyperplane<>(null, this) :
                                    new SplitSubHyperplane<>(this, null);
    }

}
