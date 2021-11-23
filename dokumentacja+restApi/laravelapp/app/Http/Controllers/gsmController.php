<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class gsmController extends Controller
{
    //Ta funkcja wyśle wiadomość na podane identyfikatory rejestracji
    //Przekazujemy również wiadomość, która w rzeczywistości jest tablicą zawierającą wiadomość 
    public function sendMessage($tokens, $message) {
        $fields = array(
            'registration_ids' => $tokens,
            'data' => $message,
        );
        //W tej funkcji wywołujemy główną metodę odpowiedzialną za wysyłanie powiadomień push
        //to jest sendPushNotification
        return $this->sendPushNotification($fields);
    }
    
 
    //Jest to główna metoda odpowiedzialna za wysyłanie powiadomień push
    //Wyjaśniłem to już w poprzednich samouczkach 
    private function sendPushNotification($fields){

        //$accesstoken = env('GOOGLE_API_KEY');

 
        $url = 'https://android.googleapis.com/gcm/send';
 
        $headers = array();
        $headers[] = 'Content-type: application/json';
        $headers[] = 'Authorization: AAAAhhYG7Wo:APA91bHxhX6-M9ej9Yd3U_23GyjgNwXXJCiLyKjjgakTQXtsZi9pCGspnbRGSlLbzZUpGmWhiuAb0ectQd7kKXJSmbcKqO_1aAob4jEwBostHN_gLuWV6BqtBCRpmWJ7h-ewiXqMemNm';
 
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
