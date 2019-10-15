package mall.managerservice.mapper;

import bean.BaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

public interface AttrInfoMapper extends Mapper<BaseAttrInfo> {

    List<BaseAttrInfo> selectAttrValueListByValueId(@Param("valueIdStr") String valueIdStr);
}
