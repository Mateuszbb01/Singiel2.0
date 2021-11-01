<?php

namespace App\Http\Controllers;

use App\Models\Preferences;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\File;
use Illuminate\Support\Facades\Validator;
use PDO;

class PreferencesController extends Controller
{
    public function create(Request $request)
    {

        $preferences = new Preferences;
        $preferences->user_id = Auth::user()->id;
        $preferences->bornDate = $request->bornDate;
        $preferences->gender = $request->gender;
        $preferences->city = $request->city;
        $preferences->interests = $request->interests;
        //sprawdza czy jest dodane zdjecie
        if ($request->photo != '') {
            //unikalna nazwa zdjecia - data+jpg
            $photo = time() . 'jpg';
            file_put_contents('storage/' . $photo, base64_decode($request->photo));
            $preferences->photo = $photo;
        }

        // if ($request->photo != '')
        //     {
        //         $file= $request->file('image');
        //         $extension = $file->getClientOriginalExtension();
        //         $filename = time().'.'.$extension;
        //         $file->move('storage/photo', $filename);
        //         $preferences->photo = $filename;
        //     }

        $preferences->save();
        $preferences->user;
        return response()->json([
            'success' => true,
            'message' => 'Dodano preferencje',
            'preferences' => $preferences
        ]);
    }

    public function mypreferences()
    {
        
        $preferences = User::with('preferences')->where('id', Auth::user()->id)->get();
        foreach ($preferences as $preferences) {
            // użytkownik
            $preferences->user;
        }

        return response()->json([
            'success' => true,
            'preferences' => $preferences
        ]);
    }

    public function updatePreferences(Request $request){


        $validator = Validator::make($request->all(), [
            'name' => 'min:2|max:150',
            'bornDate' => 'required|date',
            'gender' => 'required',
            'city' => 'required',
            'interests' => 'required',
            'photo' => 'nullable|image'

        ]);

        if($validator->fails()){
            return response()->json(['success'=>false, $validator->errors()], 422);
        }
        else{

                $preferences = Preferences::where('user_id', Auth::user()->id)->firstOrFail();

                $preferences->user->name = $request->name;
                $preferences->bornDate = $request->bornDate;
                $preferences->gender = $request->gender;
                $preferences->city = $request->city;
                $preferences->interests = $request->interests;

                if ($request->photo != '')
                    {
                        $storage='storage/'.$preferences->photo;
                        if(File::exists($storage))
                        {
                            File::delete($storage);
                        }
                        $photo = time() . 'jpg';
                        file_put_contents('storage/' . $photo, base64_decode($request->photo));
                        $preferences->photo = $photo;
                    }

                $preferences->push();
                return response()->json([
                    'success' => true,
                    'message' => 'Zaktualizowano preferencje',
                    'preferences' => $preferences
                ]);
        }
    }
};