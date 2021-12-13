<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class EditColumnsInPreferences extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::table('preferences', function (Blueprint $table) {
            $table->string('phone_no')->nullable()->change();
            $table->string('mail')->nullable()->change();
            $table->string('address')->nullable()->change();
            $table->string('company')->nullable()->change();
            $table->string('website')->nullable()->change();
            $table->string('vcard')->nullable()->change();
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
            $table->dropColumn('name')->nullable(false)->change();
            $table->dropColumn('mail')->nullable(false)->change();
            $table->dropColumn('address')->nullable(false)->change();
            $table->dropColumn('company')->nullable(false)->change();
            $table->dropColumn('website')->nullable(false)->change();
            $table->dropColumn('vcard')->nullable(false)->change();
        });
    }
}
