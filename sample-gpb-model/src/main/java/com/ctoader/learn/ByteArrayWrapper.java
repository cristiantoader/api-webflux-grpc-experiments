package com.ctoader.learn;

public class ByteArrayWrapper {
    private byte[] bytes;

    public ByteArrayWrapper() {
    }

    public ByteArrayWrapper(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
