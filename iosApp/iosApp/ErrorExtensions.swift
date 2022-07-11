//
//  ErrorExtensions.swift
//  iosApp
//
//  Created by Ilya Shevtsov on 11.07.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

extension Error {
    func asKotlin() -> CustomError? {
        let nsError = self as NSError
        guard let kotlinExc = CustomErrorsKt.getKotlinException(nsError) else { return nil }
        return kotlinExc
    }
}
