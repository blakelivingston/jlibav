/*
 * Copyright (C) 2012 Ondrej Perutka
 *
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library. If not, see 
 * <http://www.gnu.org/licenses/>.
 */
package org.libav.avcodec;

import org.bridj.Pointer;
import org.libav.LibavException;
import org.libav.avcodec.bridge.AVCodecLibrary;
import org.libav.avcodec.bridge.AVPacket;
import org.libav.bridge.LibraryManager;

/**
 * Wrapper class for the AVPacket.
 * 
 * @author Ondrej Perutka
 */
public class PacketWrapper extends AbstractPacketWrapper {

    private static final AVCodecLibrary codecLib = LibraryManager.getInstance().getAVCodecLibrary();
    
    private AVPacket packet;
    
    /**
     * Create a new wrapper for the given AVPacket.
     * 
     * @param packet an AVPacket structure
     */
    public PacketWrapper(AVPacket packet) {
        this.packet = packet;
    }
    
    @Override
    public Pointer<?> getPointer() {
        return Pointer.pointerTo(packet);
    }
    
    @Override
    public void init() {
        codecLib.av_init_packet(getPointer());
        clearWrapperCache();
    }
    
    @Override
    public void free() {
        codecLib.av_free_packet(getPointer());
        init();
    }
    
    @Override
    public int getStreamIndex() {
        if (streamIndex == null)
            streamIndex = packet.stream_index();
        
        return streamIndex;
    }

    @Override
    public void setStreamIndex(int streamIndex) {
        this.streamIndex = streamIndex;
        packet.stream_index(streamIndex);
    }
    
    @Override
    public int getSize() {
        if (size == null)
            size = packet.size();
        
        return size;
    }
    
    @Override
    public void setSize(int size) {
        this.size = size;
        packet.size(size);
    }
    
    @Override
    public Pointer<Byte> getData() {
        if (data == null)
            data = packet.data();
        
        return data;
    }
    
    @Override
    public void setData(Pointer<Byte> data) {
        this.data = data;
        packet.data(data);
    }
    
    @Override
    public int getFlags() {
        if (flags == null)
            flags = packet.flags();
        
        return flags;
    }
    
    @Override
    public void setFlags(int flags) {
        this.flags = flags;
        packet.flags(flags);
    }

    @Override
    public long getPts() {
        if (pts == null)
            pts = packet.pts();
        
        return pts;
    }

    @Override
    public void setPts(long pts) {
        this.pts = pts;
        packet.pts(pts);
    }

    @Override
    public long getDts() {
        if (dts == null)
            dts = packet.dts();
        
        return dts;
    }

    @Override
    public void setDts(long dts) {
        this.dts = dts;
        packet.dts(dts);
    }

    @Override
    public int getDuration() {
        if (duration == null)
            duration = packet.duration();
        
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
        packet.duration(duration);
    }

    @Override
    public long getConvergenceDuration() {
        if (convergenceDuration == null)
            convergenceDuration = packet.convergence_duration();
        
        return convergenceDuration;
    }

    @Override
    public void setConvergenceDuration(long convergenceDuration) {
        this.convergenceDuration = convergenceDuration;
        packet.convergence_duration(convergenceDuration);
    }

    @Override
    public long getPosition() {
        if (position == null)
            position = packet.pos();
        
        return position;
    }

    @Override
    public void setPosition(long position) {
        this.position = position;
        packet.pos(position);
    }

    @Override
    public PacketWrapper clone() {
        clearWrapperCache();
        
        PacketWrapper result = new PacketWrapper(new AVPacket());
        int res = codecLib.av_new_packet(result.getPointer(), getSize());
        if (res != 0)
            throw new RuntimeException(new LibavException(res));
        
        // FIX: change next call into copyTo(...), after the method is fixed
        Pointer<Byte> pData = getData();
        if (pData != null)
            pData.copyTo(result.getData(), getSize());
        
        result.setPts(getPts());
        result.setDts(getDts());
        result.setStreamIndex(getStreamIndex());
        result.setFlags(getFlags());
        result.setDuration(getDuration());
        result.setConvergenceDuration(getConvergenceDuration());
        result.setPosition(getPosition());
        
        return result;
    }
    
    public static PacketWrapper allocatePacket() {
        PacketWrapper result = new PacketWrapper(new AVPacket());
        result.init();
        
        return result;
    }
    
    public static PacketWrapper allocatePacket(int size) throws LibavException {
        PacketWrapper result = allocatePacket();
        int res = codecLib.av_new_packet(result.getPointer(), size);
        if (res != 0)
            throw new LibavException(res);
        
        return result;
    }
    
}
