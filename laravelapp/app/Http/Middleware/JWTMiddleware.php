<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;
use Tymon\JWTAuth\Facades\JWTAuth;

class JWTMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $message = '';

        try {
            // walidacja tokena
            JWTAuth::parseToken()->authenticate();
            return $next($request);
        } catch (\Tymon\JWTAuth\Exceptions\TokenExpiredException $e) {
            // co ma zostać wykonane gdy token się skończył
            $message = 'Token wygasł';
        } catch (\Tymon\JWTAuth\Exceptions\TokenInvalidException $e) {
            // co ma zostać wykonane gdy token jest nieprawidłowy
            $message = "Token niewłaściwy";
        } catch (\Tymon\JWTAuth\Exceptions\JWTException $e) {
            // co ma zostać wykonane gdy token nie został załączony
            $message = "Token jest wymagany";
        }
        return response()->json([
            'success' => false,
            'message' => $message
        ]);
    }
}
