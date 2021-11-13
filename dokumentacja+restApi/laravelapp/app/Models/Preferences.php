<?php

namespace App\Models;


use App\Models\User;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Preferences extends Model
{
    public function user()
    {
        return $this->belongsTo(User::class);
    }
    // public function preferences()
    // {
    //     return $this->hasOne(Preferences::class);
    // }

}
