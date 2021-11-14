<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Models\Like;
use App\Models\paired;
use App\Models\Preferences;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

use function PHPUnit\Framework\isEmpty;
use function PHPUnit\Framework\isNull;

class LikeController extends Controller
{

    public function showUser()
    {
        $id = Auth::user()->id;

       $pr = Preferences::find($id);
       $gender =  $pr->gender;


        $find_like = Like::where('user_id', $id)->pluck('user_like_id');

        $findd = Preferences::whereNotIn('user_id', $find_like)
        ->whereNotIn('gender', [$gender])
        ->whereNotIn('user_id', [$id])
        ->get();


        return response()->json([
            'success' => true,
            'users' => $findd
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

        //wyświetlenie sparowanych użytkowników 
    public function showPaired()
    {
        $id = Auth::user()->id;

        $users = Like::where('user_id', $id)->where('paired', 1)->get();

  
        if ($users->first()) {
 
                return response()->json([
                    'success' => true,
                    'paired' => $users
                ]);
            }


        return response()->json([
            'success' => false,
            'paired' => $users
        ]);
        
    }

}
