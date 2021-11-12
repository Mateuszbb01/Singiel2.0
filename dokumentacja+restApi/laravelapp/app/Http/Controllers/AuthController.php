<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use Illuminate\Support\Facades\Auth;
use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class AuthController extends Controller
{
    /**
     * Create a new AuthController instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth:api', ['except' => ['login', 'register']]);
    }

    /**
     * Get a JWT via given credentials.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
            'password' => 'required|string|min:6',
        ]);

        if ($validator->fails()) {
            return response()->json($validator->errors(), 422);
        }

        if (!$token = Auth::attempt($validator->validated())) {
            return response()->json(['success' => false, 'error' => 'Niepoprawne dane.'], 401);
        }

        return $this->createNewToken($token);
    }

    /**
     * Register a User.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function register(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|string|email|max:100|unique:users',
            'password' => 'required|string|confirmed|min:6',
        ]);

        if ($validator->fails()) {
            return response()->json($validator->errors()->toJson(), 400);
        }

        //created in database with encrypted password
        $user = User::create(array_merge(
            $validator->validated(),
            ['password' => bcrypt($request->password)]
        ));
        if (!$token = Auth::attempt($validator->validated())) {
            return response()->json(['success' => false, 'error' => 'Niepoprawne dane.'], 401);
        }


        return response()->json([
            'token' => $token,

            'success' => true,
            'user' => $user,
            /**dodać tokena!!!!! */
        ], 201);
    }


    /**
     * Log the user out (Invalidate the token).
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function logout()
    {

        Auth::logout();

        return response()->json(['success' => true, 'message' => 'Użytkownik został poprawnie wylogowany!']);
    }

    /**
     * Refresh a token.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function refresh()
    {
        return $this->createNewToken(Auth::refresh());
    }

    /**
     * Get the authenticated User.
     *
     * @return \Illuminate\Http\JsonResponse
     */
    public function userProfile()
    {
        return response()->json(Auth::user());
    }

    public function updatePassword(Request $request)
    {
        $user = User::find(Auth::user()->id);

        $userPassword = $user->password;

        $validator = Validator::make($request->all(), [
            'current_password' => 'required',
            'password' => 'required|same:confirm_password|min:7',
            'confirm_password' => 'required',
        ]);

        if (!Hash::check($request->current_password, $userPassword)) {
            return response()->json(['success' => false, 'error' => 'Niepoprawne obecne hasło.'], 422);
        } else {

            if ($validator->fails())
                return response()->json(['success' => false, $validator->errors()], 422);
        }


        $user->password = Hash::make($request->password);

        $user->save();
        return response()->json([
            'success' => true,
            'message' => 'Hasło zostało poprawnie zmienione',
        ]);
    }

    public function deleteAccount(Request $request)
    {
        $user = User::find(Auth::user()->id);
        $userPassword = $user->password;

        if (!Hash::check($request->password, $userPassword)) {
            return response()->json(['success' => false, 'error' => 'Niepoprawne hasło.'], 422);
        }
        User::destroy(Auth::user()->id);

        return response()->json([
            'success' => true,
            'message' => 'Konto zostało usunięte!',
        ]);
    }

    /**
     * Get the token array structure.
     *
     * @param  string $token
     *
     * @return \Illuminate\Http\JsonResponse
     */
    protected function createNewToken($token)
    {
        return response()->json([

            'token' => $token,
            'success' => true,
            'user' => Auth::user()

            //‎‎Ciąg tokenu dostępu wystawiony przez serwer autoryzacji.‎
            //'access_token' => $token,

            //‎‎Rodzaj tokena to jest, zazwyczaj tylko ciąg "bearer".‎
            //'token_type' => 'bearer',


            //‎Jeśli token dostępu wygaśnie, serwer powinien odpowiedzieć z czasem, na który token dostępu zostanie przyznany.‎
            //'expires_in' => Auth::factory()->getTTL() * 60,


        ]);
    }
}
