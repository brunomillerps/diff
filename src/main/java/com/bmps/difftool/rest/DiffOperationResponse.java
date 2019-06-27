package com.bmps.difftool.rest;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A representation of the results on diff
 */
public class DiffOperationResponse {

    private Object data = null;
    private boolean matched = true;

    public DiffOperationResponse data(Object data) {
        this.data = data;
        return this;
    }

    public DiffOperationResponse matched(boolean matched) {
        this.matched = matched;
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}
