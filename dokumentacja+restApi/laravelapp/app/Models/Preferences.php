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
    
    public function like()
    {
        //wyświetla wszystkie like którym zalogowany user dał like
        return $this->hasMany(Like::class, 'user_id');
    }

}
