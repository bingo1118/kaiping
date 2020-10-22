package com.smart.cloud.fire.global;

import java.util.List;

public class AssetManager {

        private List<AssetInfo> lstms;
        private int errorCode;
        private String error;

        public void setLstms(List<AssetInfo> lstms) {
            this.lstms = lstms;
        }
        public List<AssetInfo> getLstms() {
            return lstms;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }
        public int getErrorCode() {
            return errorCode;
        }

        public void setError(String error) {
            this.error = error;
        }
        public String getError() {
            return error;
        }


}
