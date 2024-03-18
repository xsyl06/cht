/*
 * MIT License
 * Copyright 2024-present cht
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cht.admin.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
/**
 * @description: 前端路由菜单类 
 * @author Wang
 * @date 2024/3/18 9:28
 * @version 1.0 
 */
@Data
public class RoutesVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 6480984596425247067L;
    /**
     * 路径
     */
    private String path;
    /**
     * 元属性
     */
    private Meta meta;
    /**
     * 路由名称，需要和前端页面的name对应
     */
    private String name;
    /**
     * 子菜单路由
     */
    private List<RoutesVo> children;
    /**
     * @description: 路由菜单的元属性
     * @author Wang
     * @date 2024/3/18 9:29
     * @version 1.0
     */
    public static class Meta{
        /**
         * 菜单图标
         */
        private String icon;
        /**
         * 菜单名称
         */
        private String title;
        /**
         * 菜单排序
         */
        private int rank;
        /**
         * 是否保存
         */
        private boolean keepAlive;
        /**
         * 菜单右侧图标
         */
        private String extraIcon;
        /**
         * 是否显示
         */
        private boolean showLink;
        /**
         * 是否展示父类菜单
         */
        private boolean showParent;
        /**
         * 菜单权限
         */
        private List<String> auths;
        /**
         * 外链地址
         */
        private String frameSrc;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public boolean isKeepAlive() {
            return keepAlive;
        }

        public void setKeepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
        }

        public String getExtraIcon() {
            return extraIcon;
        }

        public void setExtraIcon(String extraIcon) {
            this.extraIcon = extraIcon;
        }

        public boolean isShowLink() {
            return showLink;
        }

        public void setShowLink(boolean showLink) {
            this.showLink = showLink;
        }

        public boolean isShowParent() {
            return showParent;
        }

        public void setShowParent(boolean showParent) {
            this.showParent = showParent;
        }

        public List<String> getAuths() {
            return auths;
        }

        public void setAuths(List<String> auths) {
            this.auths = auths;
        }

        public String getFrameSrc() {
            return frameSrc;
        }

        public void setFrameSrc(String frameSrc) {
            this.frameSrc = frameSrc;
        }
    }
}
