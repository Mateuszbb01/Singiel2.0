<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class AddVcardColumnsToPreferencesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('preferences', function (Blueprint $table) {
            $table->string('phone_no');
            $table->string('mail');
            $table->string('address');
            $table->string('company');
            $table->string('website');
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
            $table->dropColumn('mail');
            $table->dropColumn('address');
            $table->dropColumn('company');
            $table->dropColumn('website');
        });
    }
}
