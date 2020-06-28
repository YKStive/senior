//package com.youloft.senior.utils
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//
///**
// * @author you
// * @create 2020/6/28
// * @desc 位图工具
// */
//
//
///**
// * 获取位图较短的一边
// * @receiver String 图片路径
// * @return Int
// */
//fun String.getScaleRatio(targetWith: Int): Int {
//    val options = BitmapFactory.Options()
//    options.inJustDecodeBounds = true
//    val justBitmap = BitmapFactory.decodeFile(this, options)
//    val short =  if (justBitmap.width >= justBitmap.height) justBitmap.height else justBitmap.width
//
//}
//
//
///**
// * 按照目标边进行裁剪
// * @receiver String
// * @param targetWith Int
// * @return Bitmap
// */
//fun String.cropToTarget(): Bitmap {
//    val srcBitmap = BitmapFactory.decodeFile(this)
//    val shortestSide = this.shortestSide()
//    val resultSide = if (shortestSide >= targetWith) targetWith else shortestSide
//    val scaleRatio = if(){}
//
//
//}
//
//
//
