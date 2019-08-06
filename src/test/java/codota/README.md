# Codota

[codota](https://www.codota.com)는 코드 예제를 제공해주는 plugin

[소개 영상](https://www.youtube.com/watch?v=T_wTs95wsl4)

이 예제는 okhttp3로 get 메소드를 요청해서 반환받은 json 스트링을 JSONObject를 통해 모델로 변환한다.

## 변수 선언 후 힌트 얻기

Request 생성하기

![Alt text](https://monosnap.com/image/lUNKXh1LCMUObfvnAbfdugP1vCX0Gr)

`Reqeust reqeust = ` 까지만 타이핑 하거나 그 자리에서 `ctrl+space`를 누리면 아래와 같은 힌트가 나옴

## 해당 라인에 맞는 힌트 얻기

그리고 request 정의 라인 아래(15라인)에서 `ctrl+space`를 누르면 아래와 같은 힌트가 나온다.

![Alt text](https://monosnap.com/image/qDqpdhcPfGNZul0VQoXKXXXFVfzVIU)

![Alt text](https://monosnap.com/image/doQjzxZyQCb5bc5nWLtv5xcX5J2VIQ)

19라인에서 null을 지우고 `ctlr+space`를 누르면 

![Alt text](https://monosnap.com/image/GNxEtC8u7cxcjzlhLLEDz3BIcFnE2o)

와 같은 힌트가 나온다.

## 문맥에 맞는 힌트 얻기

21 라인에서 `if()`를 입력하고 괄호 안에서 `ctlr+shift+space`를 누르면 아래와 같은 나온다.

![Alt text](https://monosnap.com/image/vzEaLJds737Uzg0yGnvKdK2L0ixlUB)

## 해당 클래스/메소드에 대한 Best Code Snippets 얻기

OkHttpClient 위에 커서를 두고 `ctrl+shift+o`를 누르면 Best Code snippets가 나온다.

![Alt text](https://monosnap.com/image/hs7oKJswrWAAC0wNNLKJfEnHWbN4FT)