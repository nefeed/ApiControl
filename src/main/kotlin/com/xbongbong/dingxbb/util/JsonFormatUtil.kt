package com.xbongbong.dingxbb.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import org.springframework.stereotype.Service

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-06-23
 * Time: 14:02
 */
/**
 * 该类提供格式化JSON字符串的方法。
 * 该类的方法formatJson将JSON字符串格式化，方便查看JSON数据。
 *
 * 例如：
 *
 * JSON字符串：["yht","xzj","zwy"]
 *
 * 格式化为：
 *
 * [
 *
 *      "yht",
 *
 *      "xzj",
 *
 *      "zwy"
 *
 * ]

 *
 * 使用算法如下：
 *
 * 对输入字符串，追个字符的遍历
 *
 * 1、获取当前字符。
 *
 * 2、如果当前字符是前方括号、前花括号做如下处理：
 *
 * （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
 *
 * （2）打印：当前字符。
 *
 * （3）前方括号、前花括号，的后面必须换行。打印：换行。
 *
 * （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
 *
 * （5）进行下一次循环。
 *
 * 3、如果当前字符是后方括号、后花括号做如下处理：
 *
 * （1）后方括号、后花括号，的前面必须换行。打印：换行。
 *
 * （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
 *
 * （3）打印：当前字符。
 *
 * （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
 *
 * （5）继续下一次循环。
 *
 * 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
 *
 * 5、打印：当前字符。

 * @author  yanghaitao
 * *
 * @version  [版本号, 2014年9月29日]
 */
@Service
class JsonFormatUtil {

    /**
     * 返回格式化JSON字符串。

     * @param json 未格式化的JSON字符串。
     * @param lineBreak 换行符。
     * *
     * @return 格式化的JSON字符串。
     */
    fun formatJson(json: String, type: String): String {
        val result = StringBuffer()
        val lineBreak = if (TYPE_NORMAL == type) ENTER_NORMAL else ENTER_WEB
        val length = json.length
        var number = 0
        var key: Char

        //遍历输入字符串。
        for (i in 0..length - 1) {
            //1、获取当前字符。
            key = json[i]

            //2、如果当前字符是前花括号、前方括号做如下处理：
            if (key == '{') {
                //（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                if (i - 1 > 0 && json[i - 1] == ':') {
                    result.append(lineBreak)
                    result.append(indent(number, type))
                }

                //（2）打印：当前字符。
                result.append(key)

                //（3）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append(lineBreak)

                //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++
                result.append(indent(number, type))

                //（5）进行下一次循环。
                continue
            }
            if (key == '[') {
                //（1）打印：当前字符。
                result.append(key)

                //（2）前方括号、前花括号，的后面必须换行。打印：换行。
                result.append(lineBreak)

                //（3）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                number++
                result.append(indent(number, type))

                //（4）进行下一次循环。
                continue
            }

            //3、如果当前字符是后方括号、后花括号做如下处理：
            if (key == ']' || key == '}') {
                //（1）后方括号、后花括号，的前面必须换行。打印：换行。
                result.append(lineBreak)

                //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                number--
                result.append(indent(number, type))

                //（3）打印：当前字符。
                result.append(key)

                //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                if (i + 1 < length && json[i + 1] != ',') {
                    result.append(lineBreak)
                }

                //（5）继续下一次循环。
                continue
            }

            //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            if (key == ',') {
                result.append(key)
                result.append(lineBreak)
                result.append(indent(number, type))
                continue
            }

            //5、打印：当前字符。
            result.append(key)
        }

        return result.toString()
    }

    /**
     * 格式化Json字符串输出换行的字符串
     */
    fun formatJson2Str(json: String): String {
        return formatJson(json, TYPE_NORMAL)
    }

    /**
     * 格式化Json字符串输出网页使用换行的字符串
     */
    fun formatJson2Html(json: String): String {
        return formatJson(json, TYPE_WEB)
    }


    private val OBJECT_TYPE_JSON by lazy { "json" }
    private val OBJECT_TYPE_ARRAY by lazy { "array" }
    /**
     * 查询json并去除过多的Array内容
     */
    fun checkJsonChildNode(json: JSONObject): JSONObject {
        for (key in json.keys) {
            val value = JSON.toJSONString(json[key])
            val type = getObjectType(value)
            if (OBJECT_TYPE_JSON == type) {
                checkJsonChildNode(JSON.parseObject(value))
            } else if (OBJECT_TYPE_ARRAY == type) {
                json.put(key, removeArrayIterator(JSON.parseArray(value)))
            }
        }
        return json
    }

    /**
     * 查询json并去除过多的Array内容
     */
    private fun getObjectType(value: String): String {
        if (value.length > 1) {
            val valueFirstChar = value.substring(0, 1).toCharArray()[0]
            val valueEndChar = value.substring(value.length - 1, value.length).toCharArray()[0]
            if ('{' == valueFirstChar && '}' == valueEndChar) {
                return OBJECT_TYPE_JSON
            } else if ('[' == valueFirstChar && ']' == valueEndChar) {
                return OBJECT_TYPE_ARRAY
            }
        }
        return "unknown"
    }

    /**
     * 去除Array中过多的内容
     */
    private fun removeArrayIterator(array: JSONArray): JSONArray {
        if (array.size > 2) {
            if (OBJECT_TYPE_JSON == getObjectType(JSON.toJSONString(array[0]))) {
                val temp1 = array[0]
                val temp2 = array[1]
                array.clear()
                array.add(temp1)
                array.add(temp2)
            }
        }
        return array
    }

    /**
     * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。

     * @param number 缩进次数。
     * *
     * @return 指定缩进次数的字符串。
     */
    private fun indent(number: Int, type: String): String {
        val result = StringBuffer()
        val space = if (TYPE_NORMAL == type) SPACE_NORMAL else SPACE_WEB
        for (i in 0..number - 1) {
            result.append(space)
        }
        return result.toString()
    }

    val TYPE_NORMAL by lazy { "normal" }
    val TYPE_WEB by lazy { "web" }
    /**
     * 换行付
     */
    val ENTER_NORMAL by lazy { "\n" }
    val ENTER_WEB by lazy { "<br />" }
    /**
     * 单位缩进字符串。
     */
    val SPACE_NORMAL by lazy { "  " }
    val SPACE_WEB by lazy { "&nbsp;&nbsp;" }

}