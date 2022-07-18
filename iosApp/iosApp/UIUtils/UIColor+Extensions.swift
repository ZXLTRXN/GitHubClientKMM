//
//  UIColorExtensions.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 23.06.2022.
//

import Foundation
import UIKit

extension UIColor {
    public convenience init?(hex: String) {
        if hex.hasPrefix("#") {
            let start = hex.index(hex.startIndex, offsetBy: 1)
            let hexColor = String(hex[start...])
            
            if hexColor.count == 6 {
                let scanner = Scanner(string: hexColor)
                var hexNumber: UInt64 = 0
                
                if scanner.scanHexInt64(&hexNumber) {
                    self.init(
                        red: CGFloat((hexNumber & 0xFF0000) >> 16) / 255.0,
                        green: CGFloat((hexNumber & 0x00FF00) >> 8) / 255.0,
                        blue: CGFloat(hexNumber & 0x0000FF) / 255.0,
                        alpha: CGFloat(1.0))
                    return
                }
            }
        }
        return nil
    }
    
    func toHex(alpha: Bool = false) -> String? {
        guard let components = cgColor.components, components.count >= 3 else {
            return nil
        }
        
        let r = Float(components[0])
        let g = Float(components[1])
        let b = Float(components[2])
        var a = Float(1.0)
        
        if components.count >= 4 {
            a = Float(components[3])
        }
        
        if alpha {
            return String(format: "%02lX%02lX%02lX%02lX", lroundf(r * 255), lroundf(g * 255), lroundf(b * 255), lroundf(a * 255))
        } else {
            return String(format: "%02lX%02lX%02lX", lroundf(r * 255), lroundf(g * 255), lroundf(b * 255))
        }
    }
}
