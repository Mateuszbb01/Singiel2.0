<?php

namespace App\Http\Controllers;

use App\Models\messages;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

use function PHPUnit\Framework\isEmpty;

class GCM extends Controller
{
    // User id from db - Global Variable
//$user_id = NULL;
 
 
/**
 * User Registration
 * url - /register
 * method - POST
 * params - name, email
 */
// $app->post('/register', function () use ($app) {
//     //Verifying parameters
//     verifyRequiredParams(array('name', 'email'));
 
//     //Response array
//     $response = array();
 
//     //Getting request parameters
//     $name = $app->request->post('name');
//     $email = $app->request->post('email');
 
//     //Vaidating email
//     validateEmail($email);
 
//     //Creating a db object
//     $db = new DbOperation();
 
//     //INserting user to database
//     $res = $db->createUser($name, $email);
 
//     //Jeśli użytkownik został utworzony
//     //Dodawanie szczegółów użytkownika do odpowiedzi
//     if ($res == USER_CREATED_SUCCESSFULLY) {
//         $response["error"] = false;
 
//         $user = $db->getUser($email);
 
//         $response['id'] = $user['id'];
//         $response['name'] = $user['name'];
//         $response['email'] = $user['email'];
 
//         echoResponse(201, $response);
 
//         //Jeśli użytkownik tworzący niepowodzenie doda błąd do odpowiedzi
//     } else if ($res == USER_CREATE_FAILED) {
//         $response["error"] = true;
//         $response["message"] = "Oops! An error occurred while registereing";
//         echoResponse(200, $response);
 
//         //Jeśli użytkownik już istnieje
//         //dodawanie danych użytkownika do odpowiedzi
//     } else if ($res == USER_ALREADY_EXISTED) {
//         $response["error"] = false;
//         $user = $db->getUser($email);
 
//         $response['id'] = $user['id'];
//         $response['name'] = $user['name'];
//         $response['email'] = $user['email'];
 
//         echoResponse(200, $response);
//     }
// });
 
/*
 * URL: /send
 * Method: POST
 * parameters: id, message
 * */
 
//Służy do wysyłania wiadomości w pokoju rozmów.
//$app->post('/send', function () use ($app) 
public function send(Request $request){
 
    //Verifying required parameters
   // verifyRequiredParams(array('id', 'message'));
 
    //Getting request parameters
    $id = Auth::user()->id;
    $message = $request->message;
   // $name = $request->name;
    $user_to_id = $request->user_to_id;
 
    //Creating a gcm object -> gcm.php
    $gcm = new gsmController();
 
    //Creating db object
    $db = new MessageController();
   // $stmt = User::where('id', Auth::user()->id)->pluck('gcmtoken')->toArray();

    //Creating response array
    $response = array();
 
    //Tworzenie tablicy zawierającej dane wiadomości
    $pushdata = array();
    //Adding title which would be the username
    //$pushdata['title'] = $name;
    //Adding the message to be sent
    $pushdata['message'] = $message;
    //Dodawanie identyfikatora użytkownika w celu zidentyfikowania użytkownika, który wysłał wiadomość
    //$pushdata['id']=$id;
 
    $stmt = User::where('id', Auth::user()->id)->pluck('gcmtoken')->toArray();
    //If message is successfully added to database
    if ($db->addMessage( $message, $user_to_id)) {
        //Wysyłanie powiadomień push z obiektem gcm
  

        $gcm->sendMessage($db->getRegistrationTokens($user_to_id), $pushdata);
        return response()->json([
            'success' => true,
            'message' => 'Dodano gcmToken',
            'token' => [$response] //dodałem zeby było w array []
           ]);
        
    } else {
        return response()->json([
            'success' => true,
            'token' => [$response] //dodałem zeby było w array []
           ]);
    }
    return response()->json([
        'success' => true,
        'message' => 'Wysłana wiadomość',
        'token' => [$response] //dodałem zeby było w array []
       ]);
}
 
/*
 * URL: /storegcmtoken/:id
 * Method: PUT
 * Parameters: token
 * */
 
//Spowoduje to przechowywanie tokenu gcm w bazie danych
//$app->put('/storegcmtoken/:id', function ($id) use ($app)
//  public function storegcmtoken($id){
//     verifyRequiredParams(array('token'));
//     $token = $app->request()->put('token');

//     $db = new MessageController();

//     $response = array();
//     if ($db->storeGCMToken($id, $token)) {
//         $response['error'] = false;
//         $response['message'] = "token stored";
//     } else {
//         $response['error'] = true;
//         $response['message'] = "Could not store token";
//     }
//     echoResponse(200, $response);
//  }
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
 
/*
 * URL: /messages
 * Method: GET
 * */
 
//Spowoduje to pobranie wszystkich wiadomości dostępnych w bazie danych w celu wyświetlenia ich w wątku. 
//$app->get('/messages', function () use ($app)


public function messages(Request $request)
{

    $user_from_id = Auth::user()->id;
    $user_to_id = $request->user_to_id;

    $stmt = messages::with('user')
    ->where('user_from_id', $user_from_id)
    ->where('user_to_id', $user_to_id)
    ->orWhere('user_from_id', $user_to_id)
    ->where('user_to_id',  $user_from_id)
    ->get()->toArray();
    

    return response()->json([
        'success' => true,
        'Message' => $stmt
    ]);

  //  $user_from_id = $request->user_from_id;
  //  $user_to_id= $request->user_to_id;
//to jest to samo co w kontrolerze MessageController bo tyllko wyciagamy dane z wiadomosci z kims i dajemy do array
    // $db = new MessageController();
    // $messages = $db->getMessages($user_from_id,$user_to_id);
    // $response = array();
    // $response['error']=false;
    // $response['messages'] = array();

    // while($row = mysqli_fetch_array($messages)){
    //     $temp = array();
    //     $temp['id']=$row['id'];
    //     $temp['message']=$row['message'];
    //     $temp['userid']=$row['user_from_id'];
    //     $temp['sentat']=$row['sentat'];
    //     $temp['name']=$row['name'];
    //     array_push($response['messages'],$temp);
    // }
    // echoResponse(200,$response);
}
 
 
}
