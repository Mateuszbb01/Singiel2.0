<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Like extends Model
{
    use HasFactory;

    public function user2()
    {
        return $this->belongsTo(User::class , 'id');
    }


    public function prefer()
    {
        return $this->belongsTo(Preferences::class, 'user_like_id', 'user_id');
    }
}
