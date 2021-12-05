<?php

namespace App\Http\Controllers;

use App\Models\Preferences;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\File;
use Illuminate\Support\Facades\Validator;
use PDO;
use JeroenDesloovere\VCard\VCard;

class PreferencesController extends Controller
{
    public function create(Request $request)
    {

        $preferences = new Preferences;
        $preferences->user_id = Auth::user()->id;
        $preferences->name = $request->name;
        $preferences->bornDate = $request->bornDate;
        $preferences->gender = $request->gender;
        $preferences->city = $request->city;
        $preferences->interests = $request->interests;
        //sprawdza czy jest dodane zdjecie

        if ($request->photo != '') {
            $photo = time() . '.jpeg';
            file_put_contents('storage/photo/' . $photo, base64_decode($request->photo));
            $preferences->photo = $photo;
            // $file = $request->file('photo');
            // $extension = $file->getClientOriginalExtension();
            //  $filename = time() . '.' . $extension;
            //  $file->move('storage/photo/', $filename);
            // $preferences->photo = $filename;
        }



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

        $preferences = Preferences::with('user')->where('id', Auth::user()->id)->get();
        // foreach ($preferences as $preferences) {
        //     // użytkownik
        //     $preferences->user;
        // }

        return response()->json([
            'success' => true,
            'preferences2' => $preferences
        ]);
    }

    public function updatePreferences(Request $request)
    {


        $validator = Validator::make($request->all(), [
            'name' => 'min:2|max:150',
            'bornDate' => 'nullable|date',
            'city' => 'nullable',
            'interests' => 'nullable',

        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, $validator->errors()], 422);
        } else {

            $preferences = Preferences::where('user_id', Auth::user()->id)->firstOrFail();

            $preferences->name = $request->name;
            $preferences->bornDate = $request->bornDate;
            $preferences->city = $request->city;
            $preferences->interests = $request->interests;

            if ($request->photo != '') {
                $storage = 'storage/photo/' . $preferences->photo;
                if (File::exists($storage)) {
                    File::delete($storage);
                }
                $photo = time() . '.jpeg';
                file_put_contents('storage/photo/' . $photo, base64_decode($request->photo));
                $preferences->photo = $photo;
            }

            $preferences->update();
            return response()->json([
                'success' => true,
                'message' => 'Zaktualizowano preferencje',
                'preferences' => $preferences
            ]);
        }
    }
    public function updateVcard(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'phone_no' => 'string|max:255|nullable',
            'mail' => 'string|max:255|nullable',
            'address' => 'string|max:255|nullable',
            'company' => 'string|max:255|nullable',
            'website' => 'string|max:255|nullable',
        ]);

        if ($validator->fails()) {
            return response()->json(['success' => false, $validator->errors()], 422);
        } else {

            $preferences = Preferences::where('user_id', Auth::user()->id)->firstOrFail();

            $preferences->phone_no = $request->phone_no;
            $preferences->mail = $request->mail;
            $preferences->address = $request->address;
            $preferences->company = $request->company;
            $preferences->website = $request->website;
            $preferences->update();


            $vcard = new VCard();
            $preferencesVcard = Preferences::where('user_id', Auth::user()->id)->firstOrFail();
            $lastname = $preferencesVcard->name;
            $firstname = '';
            $additional = '';
            $prefix = '';
            $suffix = '';

            // add personal data
            $vcard->addName($lastname, $firstname, $additional, $prefix, $suffix);

            if ($preferencesVcard->company != 'brak') {
                $vcard->addURL('https://www.facebook.com/' . $preferencesVcard->company, 'TYPE=Instagram');
            }
            if ($preferencesVcard->address != 'brak') {
                $vcard->addURL('https://www.instagram.com/' . $preferencesVcard->address, 'TYPE=Facebook');
            }
            if ($preferencesVcard->website != 'brak') {
                $vcard->addURL('https://www.tiktok.com/@' . $preferencesVcard->website, 'TYPE=TikTok');
            }
            // add work data
            //$vcard->addCompany($preferencesVcard->company);

            //$vcard->addJobtitle('Web Developer');
            // $vcard->addRole('Data Protection Officer');
            if ($preferencesVcard->mail != 'brak') {
                $vcard->addEmail($preferencesVcard->mail);
            }
            $vcard->addPhoneNumber($preferencesVcard->phone_no, 'PREF;HOME');
            // $vcard->addPhoneNumber(123456789, 'WORK');
            //$vcard->addAddress(
            //    $preferencesVcard->address
            // null,
            // 'street',
            // 'worktown',
            // null,
            // 'workpostcode',
            // 'Belgium'
            //);
            // $vcard->addLabel('street, worktown, workpostcode Belgium');
            //$vcard->addURL($preferencesVcard->website);

            $vcard->addPhoto('storage/photo/' . $preferencesVcard->photo);

            // return vcard as a string
            // return $vcard->getOutput();

            // return vcard as a download
            //return $vcard->download();

            //;
            $filename = time();
            $fullpath = $filename . '.vcf';
            $vcard->setFilename($filename);

            $vcard->setSavePath('storage/photo/');
            $vcard->save();

            $preferencesCard = Preferences::where('user_id', Auth::user()->id)->firstOrFail();

            $preferencesCard->vcard = $fullpath;

            // return response()->json([
            //     'success' => true,
            //     'preferences' => $fullpath
            // ]);


            $preferencesCard->update();
            return response()->json([
                'success' => true,
                'message' => 'Dodano vCard',
                'preferences' => $preferences
            ]);
        }
    }
};
