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
package org.usergrid.mongo.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.usergrid.mongo.MongoChannelHandler;
import org.usergrid.mongo.utils.BSONUtils;

public class OpUpdate extends OpCrud {

	int flags;
	BSONObject selector;
	BSONObject update;

	public OpUpdate() {
		opCode = OP_UPDATE;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public BSONObject getSelector() {
		return selector;
	}

	public void setSelector(BSONObject selector) {
		this.selector = selector;
	}

	public void setSelector(Map<?, ?> map) {
		selector = new BasicBSONObject();
		selector.putAll(map);
	}

	public BSONObject getUpdate() {
		return update;
	}

	public void setUpdate(BSONObject update) {
		this.update = update;
	}

	public void setUpdate(Map<?, ?> map) {
		update = new BasicBSONObject();
		update.putAll(map);
	}

	@Override
	public void decode(ChannelBuffer buffer) throws IOException {
		super.decode(buffer);
		buffer.readInt();
		fullCollectionName = readCString(buffer);
		flags = buffer.readInt();
		selector = BSONUtils.decoder().readObject(
				new ChannelBufferInputStream(buffer));
		update = BSONUtils.decoder().readObject(
				new ChannelBufferInputStream(buffer));
	}

	@Override
	public ChannelBuffer encode(ChannelBuffer buffer) {
		int l = 24; // 6 ints * 4 bytes

		ByteBuffer fullCollectionNameBytes = getCString(fullCollectionName);
		l += fullCollectionNameBytes.capacity();

		ByteBuffer selectorBytes = encodeDocument(selector);
		l += selectorBytes.capacity();

		ByteBuffer updateBytes = encodeDocument(update);
		l += updateBytes.capacity();

		messageLength = l;

		buffer = super.encode(buffer);

		buffer.writeInt(0);

		buffer.writeBytes(fullCollectionNameBytes);

		buffer.writeInt(flags);

		buffer.writeBytes(selectorBytes);

		buffer.writeBytes(updateBytes);

		return buffer;
	}

    /* (non-Javadoc)
     * @see org.usergrid.mongo.protocol.OpCrud#doOp(org.usergrid.mongo.MongoChannelHandler, org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
     */
    @Override
    public OpReply doOp(MongoChannelHandler handler, ChannelHandlerContext ctx,
            MessageEvent messageEvent) {
        return new OpReply(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OpUpdate [flags=" + flags + ", selector=" + selector
                + ", update=" + update + ", fullCollectionName="
                + fullCollectionName + ", messageLength=" + messageLength
                + ", requestID=" + requestID + ", responseTo=" + responseTo
                + ", opCode=" + opCode + "]";
    }
    
    

}
