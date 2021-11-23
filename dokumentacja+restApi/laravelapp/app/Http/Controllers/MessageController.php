<?php

namespace App\Http\Controllers;

use App\Models\messages;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;


class MessageController extends Controller
{


   //Funkcja przechowywania tokenu rejestracji gcm w bazie danych
   //BEZPOŚREDNIO użyłem w GCM
   public function storeGCMToken(Request $token)
   {

    $stmt = User::where('id', Auth::user()->id)->firstOrFail();

    $stmt->gcmtoken = $token->token;

    $stmt->update();

    return response()->json([
        'success' => true,
        'message' => 'Dodano token',
        'token' => $stmt
    ]);

   }

    //Pobieranie tokena rejestracyjnego z bazy
    //Id to osoba, która wysyła wiadomość
    //Z wykluczeniem id uzytkownika
   public function getRegistrationTokens()
   {

        $stmt = User::where('id', Auth::user()->id)->pluck('gcmtoken')->toArray();

        return response()->json([
            'success' => true,
            'Token' => $stmt
        ]);

   }


   //dodanie wiadomości do bazy
   public function addMessage($message, $user_to_id)
   {

        $stmt = new messages;
        $stmt->message = $message;
        $stmt->user_to_id = $user_to_id;
        $stmt->user_from_id = Auth::user()->id;

        $stmt->save();

        return response()->json([
            'success' => true,
            'message' => 'Dodano wiadomość',
            'Tokeny' => $stmt
        ]);

   }

   //pobieranie wiadomości z bazy
   public function getMessages(Request $request)
   
   {
    $user_from_id = Auth::user()->id;
    $user_to_id = $request->user_to_id;

    $stmt = messages::with('user')
    ->where('user_from_id', $user_from_id)
    ->where('user_to_id', $user_to_id)
    ->orWhere('user_from_id', $user_to_id)
    ->where('user_to_id',  $user_from_id)
    ->orderBy('sentat', 'ASC')->get();


    return response()->json([
        'success' => true,
        'Message' => $stmt
    ]);

   }

}
