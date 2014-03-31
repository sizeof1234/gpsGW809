/**
 * @author Jadic
 * @created 2014-2-28
 */
package com.jsecode.biz.platform;

import com.jsecode.bean.platform.PlatformMsgBean;

import java.util.List;

public interface ISendPlatData {

    public boolean addPlatformDataList(List<PlatformMsgBean> list);

    public boolean addPlatformData(PlatformMsgBean platformMsgBean);

}
