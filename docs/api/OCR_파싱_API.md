# OCR 파싱 API

OCR로 추출한 텍스트를 요청으로 받아, LLM을 이용해 영수증 내용을 파싱하여 응답합니다.
key:value는 요청 text에 따라 다르게 응답합니다.
파싱 결과에 유효한 key:value가 1개 이상 없다면 실패합니다.

## Request

### HTTP METHOD : `GET`

### url : `https://api.misik.me/reviews/ocr-parsing`
### Http Headers
- device-id: 식별할 수 있는 값   
  `미래에도 변하지 않아야함 앱을 삭제했다 다시 깔아도 안변하는 값으로 줄 수 있는지`

### RequestBody

```json
{
  "ocrText": "영수증 내용. 품명 단가 수량 카야토스트+음료세트 3,000 ..."
}
```

### Response

#### `Response Status 200 OK`

```json
{
  "parsed": [
    {
      "key": "품명",
      "value": "카야토스트+음료세트"
    },
    {
      "key": "품명",
      "value": "카야토스트+음료세트"
    },
    ...
  ]
}
```

#### `Response Status 400 Bad Request`

```json
{
  "message": "영수증 내용이 없습니다."
}
```