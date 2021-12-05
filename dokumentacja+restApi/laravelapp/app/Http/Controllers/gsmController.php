<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class gsmController extends Controller
{

    //funkcja wysyłająca wiadomość na podane identyfikatory rejestracji
    //Przekazanie również wiadomości, w tablicy zawierającą wiadomość
    public function sendMessage($token, $pushdata)
    {
        $fields = array(
            'registration_ids' => $token,
            'data' => $pushdata,

        );
        //funkcja wywołująca główną metodę odpowiedzialną za wysyłanie powiadomień push
        return $this->sendPushNotification($fields);
    }


    //Metoda odpowiedzialna za wysyłanie powiadomień push
    private function sendPushNotification($fields)
    {

        $url = 'https://fcm.googleapis.com/fcm/send';


        $headers = array(
            'Authorization: key= AAAAhhYG7Wo:APA91bHxhX6-M9ej9Yd3U_23GyjgNwXXJCiLyKjjgakTQXtsZi9pCGspnbRGSlLbzZUpGmWhiuAb0ectQd7kKXJSmbcKqO_1aAob4jEwBostHN_gLuWV6BqtBCRpmWJ7h-ewiXqMemNm',
            'Content-Type: application/json'
        );
        // $headers = array(
        //     'Authorization: key=AAAAhhYG7Wo:APA91bHxhX6-M9ej9Yd3U_23GyjgNwXXJCiLyKjjgakTQXtsZi9pCGspnbRGSlLbzZUpGmWhiuAb0ectQd7kKXJSmbcKqO_1aAob4jEwBostHN_gLuWV6BqtBCRpmWJ7h-ewiXqMemNm',
        //     'Content-Type: application/json'
        // );
        // $headers = array();
        // $headers[] = 'Authorization: key=AIzaSyC55P9tdL91PHTwe7d7KvJ_1BwfjPtWOuQ';
        // $headers[] = 'Content-type: application/json';

        $ch = curl_init();

        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

        $result = curl_exec($ch);

        curl_close($ch);

        return $result;
    }
}
