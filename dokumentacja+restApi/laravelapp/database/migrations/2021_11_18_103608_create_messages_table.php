<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateMessagesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('messages', function (Blueprint $table) {
            $table->id();
            $table->unsignedBigInteger('user_to_id');
            $table->foreign('user_to_id')->references('id')->on('users')->onDelete('cascade');
            $table->unsignedBigInteger('user_from_id');
            $table->foreign('user_from_id')->references('id')->on('users')->onDelete('cascade');
            $table->string('message', 100);
            $table->timestamp('sentat');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('messages');
    }
}
