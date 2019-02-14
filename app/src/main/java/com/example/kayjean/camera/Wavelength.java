package com.example.kayjean.camera;

import android.graphics.Color;

public final class Wavelength
{
    /**
     * true if want debugging code.
     */
    private static final boolean DEBUGGING = false;

    private static final int FIRST_COPYRIGHT_YEAR = 1998;

    /**
     * embedded copyright not displayed
     */
    @SuppressWarnings( { "UnusedDeclaration" } )
    private static final String EMBEDDED_COPYRIGHT =
            "Copyright: (c) 1998-2017 Roedy Green, Canadian Mind Products, http://mindprod.com";

    /**
     * date this verzsion released.
     */
    @SuppressWarnings( { "UnusedDeclaration" } )
    private static final String RELEASE_DATE = "2007-11-27";

    /**
     * title.
     */
    @SuppressWarnings( { "UnusedDeclaration" } )
    private static final String TITLE_STRING = "Wavelength";

    /**
     * version.
     */
    @SuppressWarnings( { "UnusedDeclaration" } )
    private static final String VERSION_STRING = "1.3";

    /**
     * purely static class, dummy constuctor.
     */
    private Wavelength()
    {
    }

    /**
     * Creates a Color object given the frequency instead of the usual RGB or HSB values.
     *
     * @param freq  frequency of the light in Terahertz (10.0e12 Hz) Will show up black outside the range 384..789
     *              TeraHertz.
     * @param gamma 0.0 .. 1.0 intensity.
     *
     * @return Color object of that frequency and intensity.
     * @noinspection SameParameterValue
     */
/*
    public static Color fColor(float freq, float gamma )
    {
        // speed of light is 299,792,458 meters/sec
        // = 2.99792458e08 m/sec
        // = 2.99792458e17 nm/sec
        // = 2.9979e458e05 nm/picosecond
        // = 299,792.458 nm/picosecond
        // 1 Terahertz = 1 cycle per picosecond
        return wvColor( 299792.458f / freq, gamma );
    } // end fColor
*/

    /**
     * Creates    a Color object given the wavelength instead of the usual RGB or HSB values.
     *
     * @param wl    wavelength of the light in nanometers. Will show up black outside the range 380..780 nanometers.
     * @param gamma 0.0 .. 1.0 intensity.
     *
     * @return Color object with tha tha wavelength and intensity
     * @noinspection StandardVariableNames
     */
    public static int wvColor( float wl, float gamma )
    {
        /**
         * red, green, blue component in range 0.0 .. 1.0.
         */
        float r = 0;
        float g = 0;
        float b = 0;
        /**
         * intensity 0.0 .. 1.0
         * based on drop off in vision at low/high wavelengths
         */
        float s = 1;
        /**
         * We use different linear interpolations on different bands.
         * These numbers mark the upper bound of each band.
         * Wavelengths of the various bandbs.
         */
        final float[] bands =
                { 380, 420, 440, 490, 510, 580, 645, 700, 780, Float.MAX_VALUE };
        /**
         * Figure out which band we fall in.  A point on the edge
         * is considered part of the lower band.
         */
        int band = bands.length - 1;
        for ( int i = 0; i < bands.length; i++ )
        {
            if ( wl <= bands[ i ] )
            {
                band = i;
                break;
            }
        }
        switch ( band )
        {
            case 0:
                /* invisible below 380 */
                // The code is a little redundant for clarity.
                // A smart optimiser can remove any r=0, g=0, b=0.
                r = 0;
                g = 0;
                b = 0;
                s = 0;
                break;
            case 1:
                /* 380 .. 420, intensity drop off. */
                r = ( 440 - wl ) / ( 440 - 380 );
                g = 0;
                b = 1;
                s = .3f + .7f * ( wl - 380 ) / ( 420 - 380 );
                break;
            case 2:
                /* 420 .. 440 */
                r = ( 440 - wl ) / ( 440 - 380 );
                g = 0;
                b = 1;
                break;
            case 3:
                /* 440 .. 490 */
                r = 0;
                g = ( wl - 440 ) / ( 490 - 440 );
                b = 1;
                break;
            case 4:
                /* 490 .. 510 */
                r = 0;
                g = 1;
                b = ( 510 - wl ) / ( 510 - 490 );
                break;
            case 5:
                /* 510 .. 580 */
                r = ( wl - 510 ) / ( 580 - 510 );
                g = 1;
                b = 0;
                break;
            case 6:
                /* 580 .. 645 */
                r = 1;
                g = ( 645 - wl ) / ( 645 - 580 );
                b = 0;
                break;
            case 7:
                /* 645 .. 700 */
                r = 1;
                g = 0;
                b = 0;
                break;
            case 8:
                /* 700 .. 780, intensity drop off */
                r = 1;
                g = 0;
                b = 0;
                s = .3f + .7f * ( 780 - wl ) / ( 780 - 700 );
                break;
            case 9:
                /* invisible above 780 */
                r = 0;
                g = 0;
                b = 0;
                s = 0;
                break;
        } // end switch
        // apply intensity and gamma corrections.
        s *= gamma;
        r *= s;
        g *= s;
        b *= s;
        return Color.rgb( r, g, b );
    } // end wvColor
}