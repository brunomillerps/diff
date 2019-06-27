package com.bmps.difftool.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("diff_objects")
public class DiffObject {

    private String id;
    private byte[] left;
    private byte[] right;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getLeft() {
        return left;
    }

    public void setLeft(byte[] left) {
        this.left = left;
    }

    public byte[] getRight() {
        return right;
    }

    public void setRight(byte[] right) {
        this.right = right;
    }

    public DiffObject withId(String id) {
        this.id = id;
        return this;
    }

    public DiffObject withLeft(byte[] leftObject) {
        this.left = leftObject;
        return this;
    }

    public DiffObject withRight(byte[] rightObject) {
        this.right = rightObject;
        return this;
    }

    @Override
    public String toString() {
        return "DiffObject{" +
                "id=" + id +
                ", left=" + new String(left) +
                ", right=" + new String(right) +
                '}';
    }
}
