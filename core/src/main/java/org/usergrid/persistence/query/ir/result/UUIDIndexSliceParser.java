/*******************************************************************************
 * Copyright 2012 Apigee Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.usergrid.persistence.query.ir.result;

import java.nio.ByteBuffer;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.UUIDSerializer;

import org.usergrid.persistence.cassandra.IndexUpdate;
import org.usergrid.utils.UUIDUtils;


/**
 * Parser for reading and writing secondary index composites
 * 
 * @author tnine
 *
 */
public class UUIDIndexSliceParser implements SliceParser<UUID> {

  private static final UUIDSerializer SERIALIZER = UUIDSerializer.get();

  /* (non-Javadoc)
   * @see org.usergrid.persistence.query.ir.result.SliceParser#parse(java.nio.ByteBuffer)
   */
  @Override
  public UUID parse(ByteBuffer buff) {
    return SERIALIZER.fromByteBuffer(buff.duplicate());
  }

  /* (non-Javadoc)
   * @see org.usergrid.persistence.query.ir.result.SliceParser#getUUID(java.lang.Object)
   */
  @Override
  public UUID getUUID(UUID value) {
    return value;
  }

  @Override
  public Object getValue(UUID value) {
    throw new UnsupportedOperationException("Getting the value is not supported on uuid lists");
  }

  /* (non-Javadoc)
   * @see org.usergrid.persistence.query.ir.result.SliceParser#serialize(java.lang.Object)
   */
  @Override
  public ByteBuffer serialize(UUID type) {
    return SERIALIZER.toByteBuffer(type);
  }

}
