/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.kafka.controller;

import org.apache.kafka.metadata.placement.PartitionAssignment;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;


class PartitionReassignmentReplicas {
    private final List<Integer> removing;
    private final List<Integer> adding;
    private final List<Integer> replicas;

    private static Set<Integer> calculateDifference(List<Integer> a, List<Integer> b) {
        Set<Integer> result = new TreeSet<>(a);
        result.removeAll(b);
        return result;
    }

    PartitionReassignmentReplicas(
        PartitionAssignment currentAssignment,
        PartitionAssignment targetAssignment
    ) {
        Set<Integer> removing = calculateDifference(currentAssignment.replicas(), targetAssignment.replicas());
        this.removing = new ArrayList<>(removing);
        Set<Integer> adding = calculateDifference(targetAssignment.replicas(), currentAssignment.replicas());
        this.adding = new ArrayList<>(adding);
        this.replicas = new ArrayList<>(targetAssignment.replicas());
        this.replicas.addAll(removing);
    }

    List<Integer> removing() {
        return removing;
    }

    List<Integer> adding() {
        return adding;
    }

    List<Integer> replicas() {
        return replicas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(removing, adding, replicas);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PartitionReassignmentReplicas)) return false;
        PartitionReassignmentReplicas other = (PartitionReassignmentReplicas) o;
        return removing.equals(other.removing) &&
            adding.equals(other.adding) &&
            replicas.equals(other.replicas);
    }

    @Override
    public String toString() {
        return "PartitionReassignmentReplicas(" +
            "removing=" + removing + ", " +
            "adding=" + adding + ", " +
            "replicas=" + replicas + ")";
    }
}
