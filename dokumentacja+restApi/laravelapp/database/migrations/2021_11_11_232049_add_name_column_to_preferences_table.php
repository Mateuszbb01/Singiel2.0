<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AddNameColumnToPreferencesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('preferences', function (Blueprint $table) {
            $table->string('name')->after('user_id');
        });

//usunięcie kolumny name  z users 
          Schema::table('users', function($table) {
             $table->dropColumn('name');
          });     
    }

    

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::table('preferences', function (Blueprint $table) {
            $table->dropColumn('name');
        });

        Schema::table('users', function($table) {
            $table->integer('name');
        });
    }
}
