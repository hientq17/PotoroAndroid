'use strict'

const md5 = require('md5');
const jwt = require('jsonwebtoken')

let refreshTokens = []

const responseBaseModel = require("../models/response-base-model")
const storeProcedureName = require("../store_procedures/store-procedure-name")
const isValidModel = require("../utils/model-filter")

const apiUser= function (dbConnection) {
    return {
        login: (req, res) => {
            let data = req.body;
            if (data.username != null && data.password != null) {  
                data.password = md5(data.password);
                dbConnection.query(storeProcedureName().login(), [JSON.stringify(data)], (err, response) => {
                    if (err) res.json(err);
                    if (response[0].length == 0)
                        res.json("Not found")
                    else{
                        if (response[1].length >0){
                            const accessToken = jwt.sign(JSON.parse(JSON.stringify(response[1]))[0], process.env.ACCESS_TOKEN_SECRET || "MySecretToken@123", { expiresIn: '2d' })
                            res.json({response: response[0],token:accessToken})
                        }
                    }
                })
            }
            else {
                res.json("Not found")
            }
        },
        signup: (req, res) => {
            let data = req.body;
            if (data.username != null && data.password != null) {
                data.password = md5(data.password);
                console.log(data);
                dbConnection.query(storeProcedureName().signup(), [JSON.stringify(data)], (err, response) => {
                    if (err) res.json(err);
                    if (response[0].length == 0)
                        res.json("Not found")
                    else
                        res.json(response[0])
                })
            }
            else {
                res.json("Not found")
            }
        },
        getUserByUsername: (req, res) => {
            if (req.query.username != null) {
                dbConnection.query(storeProcedureName().getUserByUsername(), [req.query.username], (err, response) => {
                    if (err) res.json(err);
                    if (response[0].length == 0)
                        res.json("Not found")
                    else
                        res.json(response[0][0])
                })
            }
            else {
                res.json("Not found")
            }
        },
        // insertOrUpdateUser: (req, res) => {
        //     if (isValidModel(req.body)) {
        //         let data = req.body;
        //         dbConnection.query(storeProcedureName().insertOrUpdateUser(), [JSON.stringify(data.JInput),data.Action], (err, response) => {
        //             let resData = response[0]
        //             let returnId = resData[0].returnId
        //             let outputMessage = resData[0].outputMessage
        //             let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
        //             if (err) res.json(err)
        //             res.json(model)
        //         })
        //     }
        //     else {
        //         res.json("Invalid model")
        //     }
        // },
        // deleteUser: (req, res) => {
        //     if (req.query.id > 0 ) {
        //         dbConnection.query(storeProcedureName().deleteUser(), [req.query.id], (err, response) => {
        //             let resData = response[0]
        //             let returnId = resData[0].returnId
        //             let outputMessage = resData[0].outputMessage
        //             let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
        //             if (err) res.json(err)
        //             res.json(model)
        //         })
        //     }
        //     else {
        //         res.json("Not found")
        //     }
        // }
    }
}

module.exports = apiUser