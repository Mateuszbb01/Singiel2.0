<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\GCM;
use App\Http\Controllers\LikeController;
use App\Http\Controllers\PreferencesController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::group([
    'middleware' => 'api',
    'prefix' => 'auth'


], function ($router) {
    Route::post('/login', [AuthController::class, 'login']);
    Route::post('/register', [AuthController::class, 'register']);
    Route::get('/logout', [AuthController::class, 'logout']);
    Route::post('/refresh', [AuthController::class, 'refresh']);
    Route::get('/user-profile', [AuthController::class, 'userProfile']);
    Route::post('/user-profile/update-password', [AuthController::class, 'updatePassword'])->middleware('jwtAuth');
    Route::delete('/user-profile/delete-account', [AuthController::class, 'deleteAccount'])->middleware('jwtAuth');
    Route::get('/preferences/mypreferences', [PreferencesController::class, 'mypreferences'])->middleware('jwtAuth');
    Route::post('/preferences/create', [PreferencesController::class, 'create'])->middleware('jwtAuth');
    Route::post('/preferences/mypreferences/update', [PreferencesController::class, 'updatePreferences'])->middleware('jwtAuth');

    Route::get('/showUser', [LikeController::class, 'showUser'])->middleware('jwtAuth');
    Route::post('/likeUser', [LikeController::class, 'likeUser'])->middleware('jwtAuth');
    Route::get('/showPairedUser', [LikeController::class, 'showPaired'])->middleware('jwtAuth');

    ///
    Route::post('/send', [GCM::class, 'send'])->middleware('jwtAuth');
    Route::post('/storegcmtoken', [GCM::class, 'storegcmtoken'])->middleware('jwtAuth');
    Route::get('/messages', [GCM::class, 'messages'])->middleware('jwtAuth');




});



// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });
