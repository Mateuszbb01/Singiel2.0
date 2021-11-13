<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\Like;
use App\Models\paired;
use App\Models\Preferences;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class LikeController extends Controller
{
    

    //! wyszukać wszystkich użytkowników !oprócz siebie-ok i po przeciwnej płci ta ( powiązaniem paired?)
    // i ich wyświetlić ze zdjeciem i miastem opisem i wiekiem 
    // za pomocą user_id = Auth::user()->id; wyszukamy wiersze w których jest uzytkownik  

//////////dołożyć wyswietlanie ze jesli juz kogos swipowal nie wyswietli sie 

    public function showUser()
    {
        $id = Auth::user()->id;

        $pr = Preferences::find($id);
        $gender =  $pr->gender;
        
        //  $users = Preferences::with('user')
        //  ->whereNotIn('gender', [$gender])
        //  ->whereNotIn('user_id', [$id])
        //  ->get();

        $users = Preferences::with('user')
        ->whereNotIn('gender', [$gender])
        ->whereNotIn('user_id', [$id])
        ->get();
        return response()->json([
            'success' => true,
            'users' => $users
        ]);
    }



    public function likeUser(Request $request)
    {
                
        $like = new Like;
        $like->user_id = Auth::user()->id;
        $like->user_like_id = $request->user_like_id;
        $like->like = $request->like;

        $like->save();
        $like->like;


        if($request->like == 1)
        {
                //sprawdzam czy zalogowany uzytkownik sparował się z drugim użytkownikiem
             
            $check = Like::where('user_id', Auth::user()->id)
            ->where('user_like_id', $request->user_like_id)
            ->where('like', 1)
            ->first();

            if(!is_null($check))
            {
            
                     //sprawdzam czy drugi uzytkownik sparował  się z zalogowanym użytkownikiem
                $check_like = Like::where('user_id', $request->user_like_id)
                ->where('user_like_id', Auth::user()->id)
                ->where('like', 1)
                ->first();

                if(!is_null($check_like))
                {     

                    if($request->like == 1)
                    {
                        $paired = Like::where('user_id', Auth::user()->id)
                        ->where('user_like_id', $request->user_like_id)
                        ->first();

                        $paired->paired = 1;

                        $paired->update();

                        $paired2 = Like::where('user_id', $request->user_like_id)
                        ->where('user_like_id', Auth::user()->id)
                        ->first();

                        $paired2->paired = 1;

                        $paired2->update();

                        // return response()->json([
                        //     'success' => true,
                        //     'message' => 'Zaktualizowano preferencje',
                        //     'likes' => $paired
                        // ]);
                    }
                }
            }
        
        }

        return response()->json([
            'success' => true,
            'message' => 'Dodano like',
            'like' => $like,
        ]);

    }

}
