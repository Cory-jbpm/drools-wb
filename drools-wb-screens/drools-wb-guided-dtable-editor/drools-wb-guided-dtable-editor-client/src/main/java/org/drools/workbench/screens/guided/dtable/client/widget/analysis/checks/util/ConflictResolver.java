/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.screens.guided.dtable.client.widget.analysis.checks.util;

import org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache.util.HasUUID;
import org.drools.workbench.screens.guided.dtable.client.widget.analysis.cache.util.maps.InspectorList;

public class ConflictResolver {

    private final InspectorList collection;
    private final Conflicts     conflicts;

    public ConflictResolver( final InspectorList list,
                             final boolean record ) {
        this.collection = list;
        conflicts = new Conflicts( record );
    }

    private Conflict isConflicting( final InspectorList otherCollection ) {

        if ( collection == null || otherCollection == null ) {
            return Conflict.EMPTY;
        }

        for ( Object o : collection ) {
            if ( o instanceof IsConflicting ) {
                final Conflict conflict = hasConflictingObjectInList( otherCollection,
                                                                      ( IsConflicting ) o );
                if ( conflict.foundIssue() ) {
                    return new Conflict( collection,
                                         otherCollection,
                                         conflict );
                }
            }
        }

        return Conflict.EMPTY;
    }

    private static Conflict getConflictingObjects( final InspectorList collection,
                                                   final IsConflicting isConflicting ) {

        if ( isConflicting == null || collection == null ) {
            return Conflict.EMPTY;
        }

        for ( final Object other : collection ) {
            return isConflicting( isConflicting,
                                  ( HasUUID ) other );
        }

        return Conflict.EMPTY;
    }

    private static Conflict hasConflictingObjectInList( final InspectorList collection,
                                                        final IsConflicting isConflicting ) {
        return getConflictingObjects( collection,
                                      isConflicting );
    }

    static Conflict isConflicting( final HasUUID isConflicting,
                                   final HasUUID other ) {
        if ( isConflicting instanceof IsConflicting ) {
            if ( (( IsConflicting ) isConflicting).conflicts( other ) ) {
                return new Conflict( isConflicting,
                                     other );
            }
        }
        return Conflict.EMPTY;
    }

    public Conflict resolveConflict( final InspectorList otherCollection ) {
        final Conflict first = conflicts.get( otherCollection.getUuidKey() );

        if ( first != null ) {
            if ( first.doesRelationStillExist() ) {
                return first;
            } else {
                // Clean conflict
                conflicts.remove( first );

                // Restart resolution
                return resolveConflict( otherCollection );
            }

        } else {

            final Conflict conflict = isConflicting( otherCollection );

            if ( conflict.foundIssue() ) {
                conflicts.add( conflict );
                return conflict;
            } else {
                return Conflict.EMPTY;
            }
        }
    }
}