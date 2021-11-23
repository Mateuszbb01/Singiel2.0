<?php

namespace App\Http\Controllers;

use App\Models\messages;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

use function PHPUnit\Framework\isEmpty;

class GCM extends Controller
{
   
public function send(Request $request){

 
    $id = Auth::user()->id;
    $message = $request->message;
   // $name = $request->name;
    $user_to_id = $request->user_to_id;
 
    $gcm = new gsmController();
 
    $db = new MessageController();
   // $stmt = User::where('id', Auth::user()->id)->pluck('gcmtoken')->toArray();

    //Creating response array
    $response = array();
 
    //Tworzenie tablicy zawierającej dane wiadomości
    $pushdata = array();
    //dodanie tytułu do wiadomości
    //$pushdata['title'] = $name;
    //dodanie wiadomości do tablicy
    $pushdata['message'] = $message;
    //Dodawanie identyfikatora użytkownika w celu zidentyfikowania użytkownika, który wysłał wiadomość
    //$pushdata['id']=$id;
 
    $stmt = User::where('id', Auth::user()->id)->pluck('gcmtoken')->toArray();
    //poprawnie dodanie do bazy
    if ($db->addMessage( $message, $user_to_id)) {
        //Wysyłanie powiadomień push z obiektem gcm
  

        $gcm->sendMessage($db->getRegistrationTokens($user_to_id), $pushdata);
        return response()->json([
            'success' => true,
            'token' => [$pushdata] //dodałem zeby było w array []
           ]);
        
    } else {
        return response()->json([
            'success' => true,
            'token' => [$response] //dodałem zeby było w array []
           ]);
    }
    return response()->json([
        'success' => true,
        'token' => [$response] //dodałem zeby było w array []
       ]);
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

    $stmt = messages::with('user')
    ->where('user_from_id', $user_from_id)
    ->where('user_to_id', $user_to_id)
    ->orWhere('user_from_id', $user_to_id)
    ->where('user_to_id',  $user_from_id)
    ->orderBy('sentat', 'ASC')->get()->toArray();
    

    return response()->json([
        'success' => true,
        'Message' => $stmt
    ]);

  
}
 
 
}
