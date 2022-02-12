
package com.poona.agrocart.data.network.reponses;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.reponses.BaseResponse;

public class CmsResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    private List<Cms> data = null;

    public List<Cms> getData() {
        return data;
    }

    public void setData(List<Cms> data) {
        this.data = data;
    }

    public static class Cms {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("app_type")
        @Expose
        private Object appType;
        @SerializedName("slug_url")
        @Expose
        private String slugUrl;
        @SerializedName("page_title")
        @Expose
        private String pageTitle;
        @SerializedName("cms_text")
        @Expose
        private String cmsText;
        @SerializedName("cms_meta_title")
        @Expose
        private String cmsMetaTitle;
        @SerializedName("cms_meta_keywords")
        @Expose
        private String cmsMetaKeywords;
        @SerializedName("cms_description")
        @Expose
        private String cmsDescription;
        @SerializedName("status")
        @Expose
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getAppType() {
            return appType;
        }

        public void setAppType(Object appType) {
            this.appType = appType;
        }

        public String getSlugUrl() {
            return slugUrl;
        }

        public void setSlugUrl(String slugUrl) {
            this.slugUrl = slugUrl;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public String getCmsText() {
            return cmsText;
        }

        public void setCmsText(String cmsText) {
            this.cmsText = cmsText;
        }

        public String getCmsMetaTitle() {
            return cmsMetaTitle;
        }

        public void setCmsMetaTitle(String cmsMetaTitle) {
            this.cmsMetaTitle = cmsMetaTitle;
        }

        public String getCmsMetaKeywords() {
            return cmsMetaKeywords;
        }

        public void setCmsMetaKeywords(String cmsMetaKeywords) {
            this.cmsMetaKeywords = cmsMetaKeywords;
        }

        public String getCmsDescription() {
            return cmsDescription;
        }

        public void setCmsDescription(String cmsDescription) {
            this.cmsDescription = cmsDescription;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}