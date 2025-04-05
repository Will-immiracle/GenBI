package com.will.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.will.model.pojo.Chart;
import com.will.service.ChartService;
import com.will.mapper.ChartMapper;
import org.springframework.stereotype.Service;

/**
* @author zhangzan
* @description 针对表【chart(图表信息表)】的数据库操作Service实现
* @createDate 2025-04-04 22:13:24
*/
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
    implements ChartService{

}




