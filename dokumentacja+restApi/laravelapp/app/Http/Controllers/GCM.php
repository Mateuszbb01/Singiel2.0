<?php

namespace App\Http\Controllers;

use App\Models\messages;
use App\Models\Preferences;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

use function PHPUnit\Framework\isEmpty;

class GCM extends Controller
{

    public function send(Request $request)
    {

        $id = Auth::user()->id;
        $message = $request->message;
        $user_to_id = $request->user_to_id;

        $gcm = new gsmController();
        $db = new MessageController();

        $name = Preferences::where('user_id', $id)->firstOrFail();
        $nn = $name->name;


        //Tworzenie tablicy zawierającej dane wiadomości
        $pushdata = array();
        //dodanie tytułu do wiadomości
        $pushdata['title'] = $nn;
        //dodanie wiadomości do tablicy
        $pushdata['body'] = $message;
        //$pushid = array();
        $pushdata['id'] = $id;


        $token = User::where('id', $user_to_id)->pluck('gcmtoken');
        //echo $token;
        //poprawnie dodanie do bazy
        if ($db->addMessage($message, $user_to_id)) {
            //Wysyłanie powiadomień push z obiektem gcm

            $gcm->sendMessage($token, $pushdata);

            return response()->json([
                'success' => true,

                //     'token' => $token,
                //   'message' => [$pushdata] //dodałem zeby było w array []
            ]);
        } else {
            return response()->json([
                'success' => false
                //          'tokenqa' => [$response] //dodałem zeby było w array []
            ]);
        }
        //  return response()->json([
        //     'success' => true,
        //     'tokena1' => [$response] //dodałem zeby było w array []
        //    ]);
    }

    public function sendVcard(Request $request)
    {

        $id = Auth::user()->id;
        $user_to_id = $request->user_to_id;

        $gcm = new gsmController();

        $preferences = Preferences::where('user_id', $id)->firstOrFail();

        $vard_path = $preferences->vcard;
        $name = $preferences->name;
        $fullpath = 'storage/photo/' . $vard_path;


        //Tworzenie tablicy zawierającej dane wiadomości
        $pushdata = array();
        //dodanie tytułu do wiadomości
        $pushdata['title'] = $name;
        //dodanie wiadomości do tablicy
        $pushdata['body'] = "eRjpvF+lNNqxJ2wwxFWNYEvaP1quaA47LUhU9WZQDKz6y29xiZb5+g==";
        $pushdata['url'] = $fullpath;


        $token = User::where('id', $user_to_id)->pluck('gcmtoken');
        //echo $token;
        //poprawnie dodanie do bazy


        $gcm->sendMessage($token, $pushdata);

        return response()->json([
            'success' => true,

            //     'token' => $token,
            //   'message' => [$pushdata] //dodałem zeby było w array []
        ]);


        //  return response()->json([
        //     'success' => true,
        //     'tokena1' => [$response] //dodałem zeby było w array []
        //    ]);

    }


    public function storegcmtoken(Request $request)
    {
        $stmt = User::where('id', Auth::user()->id)->firstOrFail();

        $stmt->gcmtoken = $request->token;
        // $token = $request->token;

        $stmt->update();

        if (!isEmpty($stmt)) {

            return response()->json([
                'success' => false,
                'message' => 'Nie udało się dodać gcmToken',
                'token' => $stmt
            ]);
        } else {
            return response()->json([
                'success' => true,
                'message' => 'Dodano gcmToken',
                'token' => [$stmt] //dodałem zeby było w array []
            ]);
        }
    }

    public function messages(Request $request)
    {

        $user_from_id = Auth::user()->id;
        $user_to_id = $request->user_to_id;

        $stmt = messages::with('preferences')
            ->where('user_from_id', $user_from_id)
            ->where('user_to_id', $user_to_id)
            ->orWhere('user_from_id', $user_to_id)
            ->where('user_to_id',  $user_from_id)
            ->orderBy('sentat', 'ASC')->get();
        if ($stmt->first()) {

            return response()->json([
                'success' => true,
                'Message' => $stmt
            ]);
        }


        return response()->json([
            'success' => true,
            'Message' => $stmt
        ]);
    }

    // //////////////////
    //     //funkcja wysyłająca wiadomość na podane identyfikatory rejestracji
    //     //Przekazanie również wiadomości, w tablicy zawierającą wiadomość
    //     public function sendMessage($tokens, $message) {
    //         $fields = array(
    //             'registration_ids' => $tokens,
    //             'data' => $message,
    //         );
    //         //funkcja wywołująca główną metodę odpowiedzialną za wysyłanie powiadomień push
    //         return $this->sendPushNotification($fields);
    //     }


    //     //Metoda odpowiedzialna za wysyłanie powiadomień push
    //     private function sendPushNotification($fields){

    //         $url = 'https://android.googleapis.com/gcm/send';

    //         $headers = array();
    //         $headers[] = 'Content-type: application/json';
    //         $headers[] = 'Authorization: AAAAhhYG7Wo:APA91bHxhX6-M9ej9Yd3U_23GyjgNwXXJCiLyKjjgakTQXtsZi9pCGspnbRGSlLbzZUpGmWhiuAb0ectQd7kKXJSmbcKqO_1aAob4jEwBostHN_gLuWV6BqtBCRpmWJ7h-ewiXqMemNm';

    //         $ch = curl_init();

    //         curl_setopt($ch, CURLOPT_URL, $url);
    //         curl_setopt($ch, CURLOPT_POST, true);
    //         curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    //         curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    //         curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    //         curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

    //         $result = curl_exec($ch);

    //         curl_close($ch);

    //         return $result;
    //     }


}
