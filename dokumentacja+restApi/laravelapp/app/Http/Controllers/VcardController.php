<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use JeroenDesloovere\VCard\VCard;
use App\Models\Preferences;

class VcardController extends Controller
{
    public function create()
    {
        $vcard = new VCard();
        $preferences = Preferences::where('user_id', '4')->firstOrFail();
        $lastname = $preferences->name;
        $firstname = '';
        $additional = '';
        $prefix = '';
        $suffix = '';

        // add personal data
        $vcard->addName($lastname, $firstname, $additional, $prefix, $suffix);

        // add work data
        $vcard->addCompany($preferences->company);

        //$vcard->addJobtitle('Web Developer');
        // $vcard->addRole('Data Protection Officer');
        $vcard->addEmail($preferences->mail);
        $vcard->addPhoneNumber($preferences->phone_no, 'PREF;HOME');
        // $vcard->addPhoneNumber(123456789, 'WORK');
        $vcard->addAddress(
            $preferences->address
            // null,
            // 'street',
            // 'worktown',
            // null,
            // 'workpostcode',
            // 'Belgium'
        );
        // $vcard->addLabel('street, worktown, workpostcode Belgium');
        $vcard->addURL($preferences->website);

        $vcard->addPhoto('storage/photo/' . $preferences->photo);

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
        return response()->json([
            'success' => true,
            'preferences' => $fullpath
        ]);
    }
}
