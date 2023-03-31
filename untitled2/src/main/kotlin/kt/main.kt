package kt
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*



fun main() {
    var input: String = Scanner(System.`in`).nextLine()
    val inputRaw: String = input

    input = input.replace("!", "")
    val r = Regex(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")


    if ((input.elementAt(0).code in 65..122) && input.contains("뜻") && (input.endsWith("?") || input.endsWith("야"))) {
        val eng: String = english(input)

        if(eng == "recursion") println("recursion의 뜻은 recursion입니다.")
        else println("'${eng}'의 뜻은 '" + translate(eng, "en", "ko") + "'입니다.")

    } else if(input.contains("한국어로") && (input.contains("뭐") || input.endsWith("?"))) {

        var tr: String = ""
        for(i in 0..input.length) {
            if(input.elementAt(i).toString().matches(r)) {
                break;
            } else {
                tr += input.elementAt(i)
            }
        }

        val translated: String = translate(tr)

        if(translated == "재귀" || translated == "재귀하다") println("'${tr}'을(를) 한국어로 하면 '${tr}'입니다.")
        else println("'${tr}'을(를) 한국어로 하면 '${translated}'입니다.")



    } else if((input.contains("여자친구") || input.contains("여친")) && (input.contains("몇") || input.contains("뭐") || input.contains("어디") || input.endsWith("봐") || input.contains("맞"))) {
        throw NullPointerException("오류가 발생했습니다: null인 개체를 참조할 수 없습니다.")
    } else if(((input.contains("여자친구") || input.contains("여친")) && (input.endsWith("생길까") || input.endsWith("생길까?") || input.contains("생기면") || input.contains("생긴") || input.contains("생겨"))) && !input.contains("못생")) {
        throw IllegalArgumentException("Invalid verb; 동사 '생기다'는 이 문장에서 허용되지 않습니다.")
    }

    else if(input.endsWith("어로 뭐야?") || input.endsWith("어로 뭐야")) {
        input = if(input.endsWith("?")) input.substring(0, input.length-6)
        else input.substring(0, input.length-5)
        var i = input.length

        var lan: String = ""
        while(true) {
            if(input.elementAt(i).equals(" ")) {
                break
            } else {
                lan += input.elementAt(i)
                i--
            }
        }

        var tr: String = ""
        for(i in 0..input.length) {
            if(input.elementAt(i).toString().matches(r)) {
                break;
            } else {
                tr += input.elementAt(i)
            }
        }
        
        val output: String = translate(tr, lan, Test.main(tr))

        println(output)


        lan = lan.replace("한국어", "ko").replace("영어", "en").replace("일본어", "ja")


    }


    else {
        println("아직 그거에 대한 답변은 없습니다.")
    }
}








fun english(input: String): String {
    var turn = ""
    for (i in input.indices) {
        val letter: Char = input.elementAt(i)
        if (letter.code in 65..122) {
            turn += input.elementAt(i)
        } else return turn
    }
    return turn
}



fun translate(input: String, st: String, ta: String): String {

    val clientId = "klseJjk9bpWnKDpvWM4M"
    val clientSecret = "7LgPhmFSrY"
    val startLang = st
    val targetLang = ta
    var ja3: String? = null

    try {
        val text = URLEncoder.encode(input, StandardCharsets.UTF_8)
        val apiURL = "https://openapi.naver.com/v1/papago/n2mt"
        val url = URL(apiURL)
        val con = url.openConnection() as HttpURLConnection
        con.requestMethod = "POST"
        con.setRequestProperty("X-Naver-Client-Id", clientId)
        con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
        val postParams = "source=$startLang&target=${targetLang}&text=${text}"
        con.doOutput = true
        val wr = DataOutputStream(con.outputStream)
        wr.writeBytes(postParams)
        wr.flush()
        wr.close()
        val responseCode = con.responseCode
        val br: BufferedReader
        br = if (responseCode == 200) { // 정상 호출
            BufferedReader(InputStreamReader(con.inputStream))
        } else {  // 에러 발생
            BufferedReader(InputStreamReader(con.errorStream))
        }
        var inputLine: String?
        val response = StringBuffer()
        while (br.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
        }
        br.close()
        val obj = JSONObject(response.toString())
        val ja: JSONObject = obj.getJSONObject("message")
        val ja2: JSONObject = ja.getJSONObject("result")
        return ja2.getString("translatedText")
    } catch (e: Exception) {
        println((e.toString()))
        return e.toString()
    }

}

fun translate(input: String): String {
    val lan: String = Test.main(input)
    return translate(input, lan, "ko")
}
