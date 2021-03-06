<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class messages extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_from_id',
        'user_to_id',
        'message',
    ];


    public function user()
    {
        return $this->belongsTo(User::class, 'id');
    }

    public function preferences()
    {
        return $this->hasOne(Preferences::class, 'user_id', 'user_from_id');
    }

    public $timestamps = false;
}
