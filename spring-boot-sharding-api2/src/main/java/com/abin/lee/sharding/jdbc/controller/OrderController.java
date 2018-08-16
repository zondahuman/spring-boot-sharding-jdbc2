package com.abin.lee.sharding.jdbc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by abin on 2018/8/16.
 */
@RestController
@RequestMapping("/order")
@Slf4j
@Api("OrderController--api")
public class OrderController {

    @Autowired
    private Environment env;

    @ApiOperation(value = "根据id查询学生信息", notes = "查询数据库中某个的学生信息")
    @ApiImplicitParam(name = "param", value = "随机参数", paramType = "path", required = true, dataType = "Integer")
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String add(Integer param) {
        String localParam = env.getProperty("local.param");
        log.info("---------param= " + param + "  , localParam= " + localParam);
        return localParam;
    }

    @ApiOperation(value = "根据id查询订单信息", notes = "查询数据库中某个的订单信息")
    @ApiImplicitParam(name = "id", value = "订单号", paramType = "path", required = true, dataType = "Integer")
    @RequestMapping(value = "/find", method = {RequestMethod.GET, RequestMethod.POST})
    public String find(Long id) {

        log.info("---------param= " + id);
        return id + "";
    }




}