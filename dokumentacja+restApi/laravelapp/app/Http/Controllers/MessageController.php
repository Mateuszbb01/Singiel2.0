<?php

namespace App\Http\Controllers;

use App\Models\messages;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;


class MessageController extends Controller
{


   //Funkcja przechowywania tokenu rejestracji gcm w bazie danych
   // PYTANIE czy dajemy token poźniej czy przy rejestracji jak sie da

   //BEZPOŚREDNIO użyłem w GCM
   public function storeGCMToken(Request $token)
   {

    $stmt = User::where('id', Auth::user()->id)->firstOrFail();

    $stmt->gcmtoken = $token->token;

    $stmt->update();

    return response()->json([
        'success' => true,
        'message' => 'Dodano gcmToken',
        'token' => $stmt
    ]);


    //    $stmt = $this->conn->prepare("UPDATE users SET gcmtoken =? WHERE id=?");
    //    $stmt->bind_param("si", $token, $id);
    //    if ($stmt->execute())
    //        return true;
    //    return false;
   }

    //Pobieranie tokena rejestracyjnego z bazy
    //Id to osoba, która wysyła wiadomość
    //Więc wykluczamy jego token rejestracyjny, ponieważ nadawca nie wymaga powiadomienia 
   public function getRegistrationTokens()
   {

        $stmt = User::where('id', Auth::user()->id)->pluck('gcmtoken')->toArray();

        return response()->json([
            'success' => true,
            'Token' => $stmt
        ]);
    //    $stmt = $this->conn->prepare("SELECT gcmtoken FROM users WHERE NOT id = ?;");
    //    $stmt->bind_param("i",$id);
    //    $stmt->execute();
    //    $result = $stmt->get_result();
    //    $tokens = array();
    //    while($row = $result->fetch_assoc()){
    //        array_push($tokens,$row['gcmtoken']);
    //    }
    //    return $tokens;

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



    //    $stmt = $this->conn->prepare("INSERT INTO messages (message,users_id) VALUES (?,?)");
    //    $stmt->bind_param("si",$message,$id);
    //    if($stmt->execute())
    //        return true;
    //    return false;
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
    ->get();


    return response()->json([
        'success' => true,
        'Message' => $stmt
    ]);


    //    $stmt = $this->conn->prepare("SELECT a.id, a.message, a.sentat, a.users_id, b.name FROM messages a, users b WHERE a.users_id = b.id ORDER BY a.id ASC;");
    //    $stmt->execute();
    //    $result = $stmt->get_result();
    //    return $result;
   }

//        //Function to get messages from the database 
//        public function getMessages($from_id,$to_id)
//        {
//  $stmt = $this->conn->prepare("SELECT a.id, a.message, a.sentat, a.from_id, b.name, a.to_id FROM messages a, users b WHERE ((a.from_id = ? AND a.to_id = ?) OR (a.from_id = ? AND a.to_id = ?)) AND (b.id = a.from_id);");
// 		 $stmt->bind_param("iiii", $from_id, $to_id, $to_id, $from_id);
// 	     $stmt->execute();
//          $result = $stmt->get_result();
//         return $result;
//         }
}
